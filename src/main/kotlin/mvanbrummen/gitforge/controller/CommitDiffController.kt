package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.security.Principal

@Controller
class CommitDiffController(private val repositoryService: RepositoryService) {

    @GetMapping("/repo/{accountName}/{repoName}/{branch}/commits/{commitHash}")
    fun commitDiffPage(@PathVariable accountName: String, @PathVariable repoName: String,
                       @PathVariable branch: String, @PathVariable commitHash: String,
                       model: Model, principal: Principal): String {
        model.addAttribute("repoName", repoName)
        model.addAttribute("branches", repositoryService.listBranches(accountName, repoName))
        model.addAttribute("branch", branch)
        model.addAttribute("commitHash", commitHash)
        model.addAttribute("tags", repositoryService.listTags(accountName, repoName))
        model.addAttribute("username", principal.name)
        model.addAttribute("commitDiffs", repositoryService.getCommitDiff(accountName, repoName, branch, commitHash))

        return "commitDiff"
    }
}