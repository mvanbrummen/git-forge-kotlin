package mvanbrummen.gitforge.repository

import org.jooq.DSLContext
import org.jooq.generated.Tables.ACCOUNT
import org.jooq.generated.Tables.REPOSITORY
import org.jooq.generated.tables.pojos.Repository
import org.springframework.stereotype.Service

@Service
class RepositoryRepository(private val dsl: DSLContext) {

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
                .columns(REPOSITORY.ID, REPOSITORY.ACCOUNT_ID, REPOSITORY.NAME, REPOSITORY.DESCRIPTION)
                .values(repository.id, repository.accountId, repository.name, repository.description)
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