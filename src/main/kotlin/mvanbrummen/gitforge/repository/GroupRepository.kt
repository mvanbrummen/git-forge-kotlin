package mvanbrummen.gitforge.repository

import org.jooq.DSLContext
import org.jooq.generated.Tables.GROUP
import org.jooq.generated.tables.pojos.Group
import org.springframework.stereotype.Service

@Service
class GroupRepository(private val dsl: DSLContext) {

    fun getAllGroups(): List<Group> {
        return dsl.select()
                .from(GROUP)
                .fetchInto(Group::class.java)
    }

    fun insertGroup(name: String, description: String, isPrivate: Boolean): Int {
        return dsl.insertInto(GROUP)
                .columns(GROUP.NAME, GROUP.DESCRIPTION, GROUP.IS_PRIVATE)
                .values(name, description, isPrivate)
                .execute()
    }
}