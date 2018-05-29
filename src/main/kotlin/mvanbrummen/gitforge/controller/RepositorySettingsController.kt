package mvanbrummen.gitforge.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RepositorySettingsController {

    @GetMapping("/repo/{accountName}/{repoName}/settings")
    fun repoSettingsPage(@PathVariable accountName: String, @PathVariable repoName: String): String {
        return "repositorySettings"
    }
}