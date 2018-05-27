package mvanbrummen.gitforge.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RepositoryRepository : CrudRepository<mvanbrummen.gitforge.domain.Repository, UUID> {

    fun findByAccount_Username(username: String): List<mvanbrummen.gitforge.domain.Repository>
}