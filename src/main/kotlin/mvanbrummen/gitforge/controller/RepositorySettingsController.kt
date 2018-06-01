package mvanbrummen.gitforge.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RepositorySettingsController {

    @GetMapping("/repo/{accountName}/{repoName}/settings")
    fun repoSettingsPage(@PathVariable accountName: String, @PathVariable repoName: String,
                         model: Model): String {
        model.addAttribute("repoName", repoName)
        return "repositorySettings"
    }
}