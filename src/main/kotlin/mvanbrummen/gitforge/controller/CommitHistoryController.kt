package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.BranchService
import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.security.Principal

@Controller
class CommitHistoryController(
        private val repositoryService: RepositoryService,
        private val branchService: BranchService
) {

    @GetMapping("/repo/{accountName}/{repoName}/{branch}/commits")
    fun commitHistoryPage(@PathVariable accountName: String, @PathVariable repoName: String,
                          @PathVariable branch: String, model: Model, principal: Principal): String {
        model.addAttribute("accountName", accountName)
        model.addAttribute("repoName", repoName)
        model.addAttribute("branches", branchService.listBranches(accountName, repoName))
        model.addAttribute("tags", repositoryService.listTags(accountName, repoName))
        model.addAttribute("username", principal.name)
        model.addAttribute("commits", repositoryService.listCommits(accountName, repoName, branch))
        return "commitHistory"
    }
}