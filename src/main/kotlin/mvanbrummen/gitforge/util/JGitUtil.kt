package mvanbrummen.gitforge.util

import mvanbrummen.gitforge.util.FileUtil.gitDir
import mvanbrummen.gitforge.util.FileUtil.repositoryDir
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilter
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
open class JGitUtil {

    fun initRepository(account: String, name: String) = Git.init().setDirectory(repositoryDir(account, name)).call()

    fun openRepository(account: String, name: String): Git = Git.open(gitDir(account, name))

    fun getRepositorySummary(repository: Repository): GitRepositorySummary {
        val isClean = isRepositoryClean(repository)
        val dirContents = listDirectory(repository)
        val commits = getAllCommits(repository)
        val totalCommits = commits.size
        val lastCommit = if (commits.isEmpty()) null else commits.first()
        val branches = listBranches(repository)
        val tags = listTags(repository)
        val readme = getReadmeContents(repository)

        return GitRepositorySummary(isClean, branches, tags, totalCommits, lastCommit, dirContents, readme)
    }

    fun getAllCommits(repository: Repository): List<Commit> {
        if (isRepositoryClean(repository)) return listOf()

        val git = Git(repository)
        val revCommits = git.log().all().call()

        return revCommits.map {
            Commit(
                    it.name,
                    it.committerIdent.name,
                    it.shortMessage,
                    it.commitTime,
                    it.parents.map { it.name }
            )
        }
    }

    fun diffCommits(repository: Repository, oldSha: String, newSha: String): List<CommitDiff> {
        val oldTreeParser = prepareTreeParser(repository, oldSha)
        val newTreeParser = prepareTreeParser(repository, newSha)

        val git = Git(repository)

        val out = ByteArrayOutputStream()
        val df = DiffFormatter(out)
        df.setRepository(git.repository)

        val diffs = df.scan(oldTreeParser, newTreeParser)

        return diffs.map {
            df.format(df.toFileHeader(it))
            CommitDiff(it.newPath, out.toString())
        }
    }

    fun getAllCommitsByRef(repository: Repository, ref: String): List<Commit> {
        if (isRepositoryClean(repository)) return listOf()

        val branchName = "refs/heads/$ref"

        val git = Git(repository)
        val revCommits = git.log().add(repository.resolve(branchName)).call()

        return revCommits.map {
            Commit(
                    it.name,
                    it.committerIdent.name,
                    it.shortMessage,
                    it.commitTime,
                    it.parents.map { it.name }
            )
        }
    }

    fun listBranches(repository: Repository): List<Branch> {
        val git = Git(repository)
        val branchRefs = git.branchList().call()

        return branchRefs.map { Branch(it.name, it.name.replace("refs/heads/", ""), it.objectId.name) }
    }

    fun listTags(repository: Repository): List<Tag> {
        val git = Git(repository)
        val tagRefs = git.tagList().call()

        return tagRefs.map { Tag(it.name, it.name.replace("refs/tags/", ""), it.objectId.name) }
    }

    fun isRepositoryClean(repository: Repository): Boolean = repository.allRefs.isEmpty()

    fun listDirectory(repository: Repository): List<GitDirectoryItem> {
        if (isRepositoryClean(repository)) return listOf()

        val ref = repository.getRef(Constants.HEAD)
        val revWalk = RevWalk(repository)
        val commit = revWalk.parseCommit(ref.objectId)
        val tree = commit.tree

        val treeWalk = TreeWalk(repository)
        treeWalk.addTree(tree)
        treeWalk.setRecursive(false)

        val items = arrayListOf<GitDirectoryItem>()

        while (treeWalk.next()) {
            items.add(GitDirectoryItem(treeWalk.isSubtree, treeWalk.pathString))
        }

        return items
                .sortedBy { it.isDir }
                .reversed()
    }

    fun listDirectory(repository: Repository, path: String): List<GitDirectoryItem> {
        val params = path.split("/")

        val ref = repository.getRef(Constants.HEAD)
        val revWalk = RevWalk(repository)
        val commit = revWalk.parseCommit(ref.objectId)
        val tree = commit.tree

        val treeWalk = TreeWalk(repository)
        treeWalk.addTree(tree)
        treeWalk.setRecursive(false)
        treeWalk.setFilter(PathFilter.create(path))

        val items = arrayListOf<GitDirectoryItem>()

        while (treeWalk.next()) {
            val relPath = treeWalk.pathString.replace("$path/", "")
            if (!relPath.contains("/") && !relPath.contentEquals(path) && !params.contains(relPath)) {
                items.add(GitDirectoryItem(treeWalk.isSubtree, relPath))
            }

            if (treeWalk.isSubtree) treeWalk.enterSubtree()
        }

        return items
                .sortedBy { it.isDir }
                .reversed()
    }

    fun getReadmeContents(repository: Repository): String? {
        if (isRepositoryClean(repository)) return null

        val ref = repository.getRef(Constants.HEAD)
        val revWalk = RevWalk(repository)
        val commit = revWalk.parseCommit(ref.objectId)
        val tree = commit.tree

        val treeWalk = TreeWalk(repository)
        treeWalk.addTree(tree)
        treeWalk.setRecursive(false)
        treeWalk.setFilter(PathFilter.create("README.md"))

        if (!treeWalk.next()) return null

        val objectId = treeWalk.getObjectId(0)
        val loader = repository.open(objectId)

        return String(loader.bytes)
    }

    fun getFileContents(repository: Repository, filePath: String): String? {
        val ref = repository.getRef(Constants.HEAD)
        val revWalk = RevWalk(repository)
        val commit = revWalk.parseCommit(ref.objectId)
        val tree = commit.tree

        val treeWalk = TreeWalk(repository)
        treeWalk.addTree(tree)
        treeWalk.setRecursive(false)
        treeWalk.setFilter(PathFilter.create(filePath))

        var contents: String? = null

        while (treeWalk.next()) {
            val objectId = treeWalk.getObjectId(0)
            val loader = repository.open(objectId)

            contents = String(loader.bytes)

            if (treeWalk.isSubtree) treeWalk.enterSubtree()
        }

        return contents
    }

    private fun prepareTreeParser(repository: Repository, objectId: String): AbstractTreeIterator {
        val walk = RevWalk(repository)

        val commit = walk.parseCommit(ObjectId.fromString(objectId))
        val tree = walk.parseTree(commit.tree.id)

        val treeParser = CanonicalTreeParser()
        val reader = repository.newObjectReader()
        treeParser.reset(reader, tree.id)

        walk.dispose()

        return treeParser
    }
}

data class GitRepositorySummary(
        val isClean: Boolean,
        val branches: List<Branch>,
        val tags: List<Tag>,
        val totalCommits: Int,
        val lastCommit: Commit?,
        val items: List<GitDirectoryItem>,
        val readme: String?
)

data class GitDirectoryItem(val isDir: Boolean, val path: String)

data class Refs(val branches: List<Branch>, val tags: List<Tag>)

data class Branch(val fullName: String, val name: String, val refId: String)
data class Tag(val fullName: String, val name: String, val refId: String)

data class Commit(
        val commitHash: String,
        val committerName: String,
        val message: String,
        val commitTime: Int,
        val parents: List<String>
)

data class CommitDiff(
        val newPath: String,
        val diff: String
)