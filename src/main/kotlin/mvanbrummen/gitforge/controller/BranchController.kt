package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.BranchService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class BranchController(private val branchService: BranchService) {

    @GetMapping("/repo/{accountName}/{repoName}/branch/create")
    fun createBranchPage(@PathVariable accountName: String, @PathVariable repoName: String, model: Model): String {
        model.addAttribute("accountName", accountName)
        model.addAttribute("repoName", repoName)
        model.addAttribute("newBranchForm", NewBranchForm())
        model.addAttribute("branches", branchService.listBranches(accountName, repoName))
        return "branchCreate"
    }

    @PostMapping("/repo/{accountName}/{repoName}/branch/create")
    fun createBranch(@PathVariable accountName: String, @PathVariable repoName: String, model: Model,
                     @ModelAttribute newBranchForm: NewBranchForm): String {
        branchService.createBranch(accountName, repoName, newBranchForm.branchName ?: "",
                newBranchForm.from ?: "")

        return "redirect:/repo/{accountName}/{repoName}"
    }

    @GetMapping("/repo/{accountName}/{repoName}/branch")
    fun branchPage(@PathVariable accountName: String, @PathVariable repoName: String, model: Model): String {
        model.addAttribute("repoName", repoName)
        model.addAttribute("accountName", accountName)
        model.addAttribute("branches", branchService.listBranches(accountName, repoName))
        return "branch"
    }

    @PostMapping("/repo/{accountName}/{repoName}/branch/{branchName}")
    fun deleteBranch(@PathVariable accountName: String, @PathVariable repoName: String, @PathVariable branchName: String,
                     model: Model): String {
        // TODO actually check if branch is protected
        if (branchName == "master") {
            return "redirect:/repo/{accountName}/{repoName}/branch"
        }

        branchService.deleteBranch(accountName, repoName, branchName)

        return "redirect:/repo/{accountName}/{repoName}/branch"
    }
}

data class NewBranchForm(
        val branchName: String? = null,
        val from: String? = null
)