package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.repository.RepositoryRepository
import org.jooq.generated.public_.tables.pojos.Repository
import org.springframework.stereotype.Service

@Service
class RepositoryService(val repository: RepositoryRepository) {

    fun findByRepositoriesByAccount(username: String): List<Repository> {
        return repository.findByRepositoriesByAccount(username)
    }
}