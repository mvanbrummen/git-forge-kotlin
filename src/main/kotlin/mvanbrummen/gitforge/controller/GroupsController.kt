package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.GroupService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.security.Principal

@Controller
class GroupsController(private val groupService: GroupService) {

    @GetMapping("/groups")
    fun allGroupsPage(model: Model): String {
        model.addAttribute("groups", groupService.getAllGroups())
        return "groupsDashboard"
    }

    @GetMapping("/groups/new")
    fun createRepositoryPage(model: Model): String {
        model.addAttribute("newGroupForm", CreateGroupForm())

        return "createGroup"
    }

    @PostMapping("/groups/new")
    fun createNewRepository(@ModelAttribute newGroupForm: CreateGroupForm,
                            model: Model, principal: Principal): String {
        with(newGroupForm) {
            groupService.createGroup(name ?: "",
                    description ?: "", false)

        }

        return "redirect:/groups"
    }

    @GetMapping("/groups/{groupName}")
    fun groupsPage(@PathVariable groupName: String, model: Model): String {

        return "group"
    }
}

data class CreateGroupForm(
        val name: String? = null,
        val description: String? = null)