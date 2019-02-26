package com.atlassian.performance.tools.infrastructure.api.jvm

import com.atlassian.performance.tools.ssh.api.Ssh
import com.atlassian.performance.tools.ssh.api.SshHost
import com.atlassian.performance.tools.ssh.api.auth.PublicKeyAuthentication
import com.atlassian.performance.tools.sshubuntu.api.SshUbuntuContainer
import org.junit.Test
import kotlin.concurrent.thread

class ThreadDumpIT {
    @Test
    fun shouldGatherThreadDump() {
//        SshUbuntuContainer().start().use { sshUbuntu ->
//            val sshHost = Ssh(with(sshUbuntu.ssh) {
//                SshHost(
//                    ipAddress = ipAddress,
//                    userName = userName,
//                    authentication = PublicKeyAuthentication(privateKey),
//                    port = port
//                )
//            })
//            val jdk = OracleJDK()
//            sshHost.newConnection().use { sshConnection ->
//                jdk.install(sshConnection)
//                sshConnection.execute("""echo "public class Test { public static void main(String[] args) { try { Thread.sleep(java.time.Duration.ofMinutes(1).toMillis()); } catch (InterruptedException e) { throw new RuntimeException(e); } }}" > Test.java """.trimIndent())
//                sshConnection.execute("${jdk.use()} Test.java")
//            }
//            thread(isDaemon = true) {
//                    sshHost.newConnection().execute("${jdk.use()} Test.java")
//            }
//        }
    }
}