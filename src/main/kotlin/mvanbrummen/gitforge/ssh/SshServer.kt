package mvanbrummen.gitforge.ssh


import org.apache.sshd.git.pack.GitPackCommandFactory
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.server.session.ServerSession
import org.slf4j.LoggerFactory
import java.security.PublicKey

class SshServer(val port: Int, val rootDir: String) {
    private val logger = LoggerFactory.getLogger(SshServer::class.java)

    private val sshd = org.apache.sshd.server.SshServer.setUpDefaultServer()

    private fun configure() {
        logger.info("Configuring SSH server...")
        sshd.setPort(port)
        sshd.setPublickeyAuthenticator({ username: String, key: PublicKey, session: ServerSession -> true }) // TODO implement
        sshd.setKeyPairProvider(SimpleGeneratorHostKeyProvider())

        sshd.setCommandFactory(GitPackCommandFactory(rootDir))
    }

    fun start() {
        logger.info("Starting SSH server...")
        configure()

        sshd.start()
        logger.info("Started SSH server!")
    }
}
