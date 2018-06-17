package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.repository.GroupRepository
import org.jooq.generated.tables.pojos.Group
import org.springframework.stereotype.Service

@Service
class GroupService(private val groupRepository: GroupRepository) {

    fun getAllGroups(): List<Group> = groupRepository.getAllGroups()

    fun getGroup(groupName: String) {

    }

    fun createGroup(name: String, description: String, isPrivate: Boolean) {
        groupRepository.insertGroup(name, description, false)
    }
}