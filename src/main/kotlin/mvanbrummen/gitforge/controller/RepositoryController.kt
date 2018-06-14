package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.security.Principal

@Controller
class RepositoryController(private val repositoryService: RepositoryService) {

    @GetMapping("/repo/{accountName}/{repoName}")
    fun repositoryPage(@PathVariable accountName: String, @PathVariable repoName: String, model: Model,
                       principal: Principal): String {
        val repository = repositoryService.getRepositorySummary(accountName, repoName)
        model.addAttribute("repository", repository)
        model.addAttribute("repoName", repoName)
        model.addAttribute("branches", repository.repoSummary.branches)
        model.addAttribute("tags", repository.repoSummary.tags)
        model.addAttribute("username", principal.name)
        model.addAttribute("accountName", accountName)
        return "repository"
    }
}