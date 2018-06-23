package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.api.RepositorySummary
import mvanbrummen.gitforge.repository.RepositoryRepository
import mvanbrummen.gitforge.repository.UserRepository
import mvanbrummen.gitforge.util.*
import mvanbrummen.gitforge.util.DateFormatter.formatUnixTimestamp
import org.jooq.generated.tables.pojos.Repository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RepositoryService(
        private val repository: RepositoryRepository,
        private val userRepository: UserRepository,
        private val gitUtil: JGitUtil) {

    fun findByRepositoriesByAccount(username: String): List<Repository> {
        return repository.findRepositoriesByAccount(username)
    }

    fun saveRepository(username: String, repoName: String, description: String, isPrivate: Boolean) {
        gitUtil.initRepository(username, repoName)

        repository.saveRepository(Repository(
                UUID.randomUUID(),
                userRepository.getUser(username)?.id,
                repoName,
                description
        ))
    }

    fun getRepositorySummary(username: String, repoName: String): RepositorySummary {
        val git = gitUtil.openRepository(username, repoName)

        val repo = repository.findRepository(username, repoName)

        return RepositorySummary(repo.description, gitUtil.getRepositorySummary(git))
    }

    fun getRepositoryItemsByPath(username: String, repoName: String, path: String): List<GitDirectoryItem> {
        val git = gitUtil.openRepository(username, repoName)

        return gitUtil.listDirectory(git.repository, path)
    }

    fun deleteRepository(username: String, repoName: String) = repository.delete(username, repoName)

    fun getFileContentsByPath(username: String, repoName: String, path: String): String {
        val git = gitUtil.openRepository(username, repoName)

        return gitUtil.getFileContents(git.repository, path) ?: throw RuntimeException("no file $path")
    }

    fun listTags(username: String, repoName: String): List<Tag> =
            gitUtil.listTags(gitUtil.openRepository(username, repoName))


    fun listCommits(username: String, repoName: String, branchName: String): Map<String, List<Commit>> {
        val git = gitUtil.openRepository(username, repoName)

        val commits = gitUtil.getAllCommits(git)

        val usernameMap = commits
                .distinctBy { it.committerName }
                .map {
                    it.committerName to userRepository.getUserByEmailAddress(it.committerEmail)?.username
                }.toMap()

        return commits
                .map { it.copy(committerName = usernameMap[it.committerName] ?: it.committerName) }
                .groupBy { formatUnixTimestamp(it.commitTime) }
    }

    fun getCommitDiff(username: String, repoName: String, branchName: String, commitHash: String): List<CommitDiff> {
        val git = gitUtil.openRepository(username, repoName)

        return gitUtil.diffCommits(git, commitHash)
    }
}