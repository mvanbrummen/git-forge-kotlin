package mvanbrummen.gitforge.repository

import org.jooq.DSLContext
import org.jooq.generated.Tables.ACCOUNT
import org.jooq.generated.tables.pojos.Account

import org.springframework.stereotype.Service
import java.util.*

@Service
class UserRepository(private val dsl: DSLContext) {

    fun getUser(username: String): Account? {
        return dsl.select()
                .from(ACCOUNT)
                .where(ACCOUNT.USERNAME.eq(username))
                .fetchOneInto(Account::class.java)
    }

    fun getUserByEmailAddress(emailAddress: String): Account? {
        return dsl.select()
                .from(ACCOUNT)
                .where(ACCOUNT.EMAIL_ADDRESS.eq(emailAddress))
                .fetchOneInto(Account::class.java)
    }

    fun saveUser(id: UUID, username: String, emailAddress: String, password: String): Int {
        return dsl.insertInto(ACCOUNT)
                .columns(ACCOUNT.ID, ACCOUNT.USERNAME, ACCOUNT.EMAIL_ADDRESS, ACCOUNT.PASSWORD)
                .values(id, username, emailAddress, password)
                .execute()
    }
}