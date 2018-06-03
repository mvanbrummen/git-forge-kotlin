package mvanbrummen.gitforge.service

import mvanbrummen.gitforge.util.FileUtil
import org.springframework.stereotype.Service
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.time.Instant

@Service
class ArchiveService {

    fun getZip(account: String, name: String, fileName: String): File {
        val unixTimestamp = Instant.now().epochSecond

        val source = FileUtil.repositoryDir(account, name)
        val zipDestination = File("${FileUtil.tmpDir().absolutePath}/${fileName}_$unixTimestamp.zip")

        ZipUtil.pack(source, zipDestination)

        return zipDestination
    }
}