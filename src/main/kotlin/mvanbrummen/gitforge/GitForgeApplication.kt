package mvanbrummen.gitforge

import mvanbrummen.gitforge.ssh.SshServer
import mvanbrummen.gitforge.util.FileUtil
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)

    SshServer(8008, FileUtil.homeDir().canonicalPath).start()
}
