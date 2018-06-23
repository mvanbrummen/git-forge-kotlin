package mvanbrummen.gitforge.repository

import org.jooq.DSLContext
import org.jooq.generated.Tables.ACCOUNT
import org.jooq.generated.Tables.REPOSITORY
import org.jooq.generated.tables.pojos.Repository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class RepositoryRepository(private val dsl: DSLContext) {

    data class RepositoryInfo(
            var id: UUID? = null,
            var accountId: UUID? = null,
            var name: String? = null,
            var description: String? = null,
            var createdAt: Timestamp? = null,
            var isPrivate: Boolean? = null,
            var username: String? = null
    )

    fun findAllPublicRepositories(): List<RepositoryInfo> {
        return dsl.select()
                .from(REPOSITORY)
                .join(ACCOUNT).on(REPOSITORY.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(REPOSITORY.IS_PRIVATE.isFalse)
                .fetchInto(RepositoryInfo::class.java)
    }

    fun findRepositoriesByAccount(username: String): List<Repository> {
        return dsl.select()
                .from(REPOSITORY)
                .join(ACCOUNT).on(REPOSITORY.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(ACCOUNT.USERNAME.eq(username))
                .fetchInto(Repository::class.java)
    }

    fun findRepository(username: String, repoName: String): Repository {
        return dsl.select()
                .from(REPOSITORY)
                .join(ACCOUNT).on(REPOSITORY.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(ACCOUNT.USERNAME.eq(username))
                .and(REPOSITORY.NAME.eq(repoName))
                .fetchOneInto(Repository::class.java)
    }

    fun saveRepository(repository: Repository): Int {
        return dsl.insertInto(REPOSITORY)
                .columns(REPOSITORY.ID, REPOSITORY.ACCOUNT_ID, REPOSITORY.NAME, REPOSITORY.DESCRIPTION, REPOSITORY.IS_PRIVATE)
                .values(repository.id, repository.accountId, repository.name, repository.description, repository.isPrivate)
                .execute()
    }

    fun delete(username: String, repoName: String) {
        val accountId = dsl.select(ACCOUNT.ID)
                .from(REPOSITORY)
                .join(ACCOUNT).on(REPOSITORY.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(ACCOUNT.USERNAME.eq(username))
                .and(REPOSITORY.NAME.eq(repoName))
                .fetchOne(ACCOUNT.ID)

        dsl.delete(REPOSITORY)
                .where(REPOSITORY.ACCOUNT_ID.eq(accountId))
                .and(REPOSITORY.NAME.eq(repoName))
                .execute()
    }
}