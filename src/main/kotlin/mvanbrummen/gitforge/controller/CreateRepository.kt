package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.security.Principal

@Controller
class CreateRepository(private val repositoryService: RepositoryService) {

    @GetMapping("/repo/new")
    fun createRepositoryPage(model: Model): String {
        model.addAttribute("newRepositoryForm", CreateRepositoryForm())

        return "createRepository"
    }

    @PostMapping("/repo/new")
    fun createNewRepository(@ModelAttribute newRepositoryForm: CreateRepositoryForm,
                            model: Model, principal: Principal): String {
        with(newRepositoryForm) {
            repositoryService.saveRepository(
                    principal.name,
                    name ?: "",
                    description ?: "",
                    private ?: false
            )
        }

        return "redirect:/repo/${principal.name}/${newRepositoryForm.name}"
    }
}

data class CreateRepositoryForm(
        val name: String? = null,
        val description: String? = null,
        var private: Boolean? = null)