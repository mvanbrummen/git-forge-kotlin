package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DashboardController(val repositoryService: RepositoryService) {

    @GetMapping("/dashboard")
    fun getDashboardPage(model: Model): String {
        model.addAttribute("repos", repositoryService.findByRepositoriesByAccount("mvanbrummen"))

        return "dashboard"
    }
}