package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DashboardController(val repositoryService: RepositoryService) {

    @GetMapping("/dashboard")
    fun getDashboardPage(): String {

        repositoryService.getRepositoriesByAccount("mvanbrummen")

        return "dashboard"
    }
}