package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.repository.RepositoryRepository
import mvanbrummen.gitforge.repository.UserRepository
import org.jooq.generated.tables.pojos.Repository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RepositoryService(
        val repository: RepositoryRepository,
        val userRepository: UserRepository) {

    fun findByRepositoriesByAccount(username: String): List<Repository> {
        return repository.findByRepositoriesByAccount(username)
    }

    fun saveRepository(username: String, repoName: String, description: String, isPrivate: Boolean) {
        repository.saveRepository(Repository(
                UUID.randomUUID(),
                userRepository.getUser(username)?.id,
                repoName,
                description
        ))
    }
}