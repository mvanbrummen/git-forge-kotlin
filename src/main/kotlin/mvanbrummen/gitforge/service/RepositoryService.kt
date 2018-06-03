package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.api.RepositorySummary
import mvanbrummen.gitforge.repository.RepositoryRepository
import mvanbrummen.gitforge.repository.UserRepository
import mvanbrummen.gitforge.util.GitDirectoryItem
import mvanbrummen.gitforge.util.JGitUtil
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

        return RepositorySummary(repo.description, gitUtil.getRepositorySummary(git.repository))
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

}