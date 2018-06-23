package mvanbrummen.gitforge.ssh


import org.apache.sshd.git.pack.GitPackCommandFactory
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.slf4j.LoggerFactory

class SshServer(val port: Int, val rootDir: String) {
    private val logger = LoggerFactory.getLogger(SshServer::class.java)

    private val sshd = org.apache.sshd.server.SshServer.setUpDefaultServer()

    private fun configure() {
        logger.info("Configuring SSH server...")
        sshd.port = port
        sshd.publickeyAuthenticator = PublickeyAuthenticator { s, publicKey, serverSession -> true }
        sshd.keyPairProvider = SimpleGeneratorHostKeyProvider()
        sshd.commandFactory = GitPackCommandFactory(rootDir)
    }

    fun start() {
        logger.info("Starting SSH server...")
        configure()

        sshd.start()
        logger.info("Started SSH server!")
    }
}
