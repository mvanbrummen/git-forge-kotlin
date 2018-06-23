package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class DashboardController(val repositoryService: RepositoryService) {

    @GetMapping("/")
    fun dashboardPage(principal: Principal, model: Model): String {
        model.addAttribute("repos", repositoryService.findByRepositoriesByAccount(principal.name))
        model.addAttribute("username", principal.name)
        return "dashboard"
    }

    @GetMapping("/explore")
    fun explorePage(principal: Principal, model: Model): String {
        model.addAttribute("repos", repositoryService.findAllPublicRepositories())
        model.addAttribute("username", principal.name)
        return "explore"
    }
}