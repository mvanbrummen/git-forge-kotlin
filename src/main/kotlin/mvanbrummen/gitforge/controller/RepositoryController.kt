package mvanbrummen.gitforge.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RepositoryController {

    @GetMapping("/repo/{accountName}/{repoName}")
    fun repositoryPage(@PathVariable accountName: String, @PathVariable repoName: String, model: Model): String {

        return "repository"
    }
}