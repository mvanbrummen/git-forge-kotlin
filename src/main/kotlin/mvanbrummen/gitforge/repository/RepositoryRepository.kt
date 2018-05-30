package mvanbrummen.gitforge.repository

import org.jooq.DSLContext
import org.jooq.generated.Tables.ACCOUNT
import org.jooq.generated.Tables.REPOSITORY
import org.jooq.generated.tables.pojos.Repository

import org.springframework.stereotype.Service

@Service
class RepositoryRepository(private val dsl: DSLContext) {

    fun findByRepositoriesByAccount(username: String): List<Repository> {
        return dsl.select()
                .from(REPOSITORY)
                .join(ACCOUNT).on(REPOSITORY.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(ACCOUNT.USERNAME.eq(username))
                .fetchInto(Repository::class.java)
    }
}