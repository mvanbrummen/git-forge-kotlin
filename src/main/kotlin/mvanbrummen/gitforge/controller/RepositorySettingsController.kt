package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import mvanbrummen.gitforge.util.FileUtil
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class RepositorySettingsController(private val repositoryService: RepositoryService) {

    @GetMapping("/repo/{accountName}/{repoName}/settings")
    fun repoSettingsPage(@PathVariable accountName: String, @PathVariable repoName: String,
                         model: Model): String {
        model.addAttribute("repoName", repoName)
        model.addAttribute("accountName", accountName)
        model.addAttribute("deleteRepoForm", DeleteRepoForm())
        return "repositorySettings"
    }

    @PostMapping("/repo/{accountName}/{repoName}")
    fun deleteRepo(@PathVariable accountName: String, @PathVariable repoName: String,
                   @ModelAttribute deleteRepoForm: DeleteRepoForm, model: Model): String {
//        if (!deleteRepoForm.repoName.equals(repoName)) {
//            println("Repo names dont match!")
//
//            return "repositorySettings"
//        }

        repositoryService.deleteRepository(accountName, repoName)

        FileUtil.deleteRepositoryDir(accountName, repoName)

        return "redirect:/"
    }

}

data class DeleteRepoForm(val repoName: String? = null)