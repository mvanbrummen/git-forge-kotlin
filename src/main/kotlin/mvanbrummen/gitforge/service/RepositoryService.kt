package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.domain.Repository
import mvanbrummen.gitforge.repository.RepositoryRepository
import org.springframework.stereotype.Service

@Service
class RepositoryService(val repository: RepositoryRepository) {

    fun getRepositoriesByAccount(username: String): List<Repository> {
        return repository.findByAccount_Username(username)
    }

}