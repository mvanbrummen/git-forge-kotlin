package mvanbrummen.gitforge.util

import mvanbrummen.gitforge.util.DateFormatter.formatUnixTimestamp
import mvanbrummen.gitforge.util.FileUtil.gitDir
import mvanbrummen.gitforge.util.FileUtil.repositoryDir
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.treewalk.EmptyTreeIterator
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilter
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
open class JGitUtil {

    fun initRepository(account: String, name: String) = Git.init().setDirectory(repositoryDir(account, name)).call()

    fun openRepository(account: String, name: String): Git = Git.open(gitDir(account, name))

    fun getRepositorySummary(git: Git): GitRepositorySummary {
        val repository = git.repository

        val isClean = isRepositoryClean(repository)
        val dirContents = listDirectory(repository)
        val commits = getAllCommits(git)
        val totalCommits = commits.size
        val lastCommit = if (commits.isEmpty()) null else commits.first()
        val branches = listBranches(git)
        val tags = listTags(git)
        val readme = getReadmeContents(repository)

        return GitRepositorySummary(isClean, branches, tags, totalCommits, lastCommit, dirContents, readme)
    }

    fun getAllCommits(git: Git): List<Commit> {
        if (isRepositoryClean(git.repository)) return listOf()

        val revCommits = git.log().all().call()

        return revCommits.map {
            Commit(
                    it.name,
                    it.committerIdent.name,
                    it.shortMessage,
                    it.commitTime,
                    formatUnixTimestamp(it.commitTime),
                    it.parents.map { it.name }
            )
        }
    }

    fun diffCommits(git: Git, commitSha: String): List<CommitDiff> {
        val repository = git.repository
        val (oldTreeParser, newTreeParser) = prepareTreeParser(repository, commitSha)

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
                    formatUnixTimestamp(it.commitTime),
                    it.parents.map { it.name }
            )
        }
    }

    fun listBranches(git: Git): List<Branch> {
        val branchRefs = git.branchList().call()

        return branchRefs.map { Branch(it.name, it.name.replace("refs/heads/", ""), it.objectId.name) }
    }

    fun listTags(git: Git): List<Tag> {
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
        treeWalk.isRecursive = false

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
        treeWalk.isRecursive = false
        treeWalk.filter = PathFilter.create(path)

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
        treeWalk.isRecursive = false
        treeWalk.filter = PathFilter.create("README.md")

        if (!treeWalk.next()) return null

        val objectId = treeWalk.getObjectId(0)
        val loader = repository.open(objectId)

        return String(loader.bytes).toHtml()
    }

    fun getFileContents(repository: Repository, filePath: String): String? {
        val ref = repository.getRef(Constants.HEAD)
        val revWalk = RevWalk(repository)
        val commit = revWalk.parseCommit(ref.objectId)
        val tree = commit.tree

        val treeWalk = TreeWalk(repository)
        treeWalk.addTree(tree)
        treeWalk.isRecursive = false
        treeWalk.filter = PathFilter.create(filePath)

        var contents: String? = null

        while (treeWalk.next()) {
            val objectId = treeWalk.getObjectId(0)
            val loader = repository.open(objectId)

            contents = String(loader.bytes)

            if (treeWalk.isSubtree) treeWalk.enterSubtree()
        }

        return contents
    }

    fun createBranch(git: Git, name: String, from: String) {
        git.branchCreate()
                .setStartPoint(from)
                .setName(name)
                .call()
    }

    private fun prepareTreeParser(repository: Repository, objectId: String): Pair<AbstractTreeIterator, AbstractTreeIterator> {
        fun getTreeParser(commit: RevCommit, walk: RevWalk): AbstractTreeIterator {
            val tree = walk.parseTree(commit.tree.id)

            val parser = CanonicalTreeParser()
            val reader = repository.newObjectReader()
            parser.reset(reader, tree.id)

            walk.dispose()

            return parser
        }

        val walk = RevWalk(repository)
        val commit = walk.parseCommit(ObjectId.fromString(objectId))

        val oldTreeParser = if (commit.parentCount > 0)
            getTreeParser(walk.parseCommit(commit.getParent(0).id), walk) else
            EmptyTreeIterator()

        val newTreeParser = getTreeParser(commit, walk)

        return Pair(oldTreeParser, newTreeParser)
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
        val commitTimePretty: String,
        val parents: List<String>
)

data class CommitDiff(
        val newPath: String,
        val diff: String
)