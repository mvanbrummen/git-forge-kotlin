package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.util.Branch
import mvanbrummen.gitforge.util.JGitUtil
import org.springframework.stereotype.Service

@Service
class BranchService(private val gitUtil: JGitUtil) {

    fun createBranch(username: String, repoName: String, branchName: String, from: String) {
        // TODO add activity event

        val git = gitUtil.openRepository(username, repoName)

        gitUtil.createBranch(git, branchName, from)
    }

    fun deleteBranch(username: String, repoName: String, branchName: String) {
        val git = gitUtil.openRepository(username, repoName)

        gitUtil.deleteBranch(git, branchName)
    }

    fun listBranches(username: String, repoName: String): List<Branch> =
            gitUtil.listBranches(gitUtil.openRepository(username, repoName))
}