package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RepositoryController(private val repositoryService: RepositoryService) {

    @GetMapping("/repo/{accountName}/{repoName}")
    fun repositoryPage(@PathVariable accountName: String, @PathVariable repoName: String, model: Model): String {
        val repository = repositoryService.getRepositorySummary(accountName, repoName)
        model.addAttribute("repository", repository)
        model.addAttribute("repoName", repoName)
        return "repository"
    }
}