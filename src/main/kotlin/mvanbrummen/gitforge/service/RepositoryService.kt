package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.api.RepositorySummary
import mvanbrummen.gitforge.repository.RepositoryRepository
import mvanbrummen.gitforge.repository.UserRepository
import mvanbrummen.gitforge.util.*
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

    fun listBranches(username: String, repoName: String): List<Branch> =
            gitUtil.listBranches(gitUtil.openRepository(username, repoName))

    fun listTags(username: String, repoName: String): List<Tag> =
            gitUtil.listTags(gitUtil.openRepository(username, repoName))

    fun createBranch(username: String, repoName: String, branchName: String, from: String) {
        // TODO add activity event

        val git = gitUtil.openRepository(username, repoName)

        gitUtil.createBranch(git, branchName, from)
    }

    fun listCommits(username: String, repoName: String, branchName: String): List<Commit> {
        val git = gitUtil.openRepository(username, repoName)

        return gitUtil.getAllCommits(git)
    }
}