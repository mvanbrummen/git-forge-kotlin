package mvanbrummen.gitforge.util

import java.io.File

object FileUtil {

    fun homeDir(): File = createDir(System.getProperty("GITFORGE_HOME", ".gitforge"))

    fun tmpDir(): File = createDir(System.getProperty("GITFORGE_TMP", "${homeDir().getAbsolutePath()}/tmp"))

    fun repositoryDir(account: String, name: String): File = createDir("${homeDir().getAbsolutePath()}/$account/$name")

    fun gitDir(account: String, name: String): File = createDir("${homeDir().getAbsolutePath()}/$account/$name/.git")

    fun deleteRepositoryDir(account: String, name: String) {
        repositoryDir(account, name).deleteRecursively()
    }

    private fun createDir(path: String): File {
        val dir = File(path)
        dir.mkdirs()

        return dir
    }
}
