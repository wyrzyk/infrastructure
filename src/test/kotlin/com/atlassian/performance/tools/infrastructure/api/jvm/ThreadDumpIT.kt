package com.atlassian.performance.tools.infrastructure.api.jvm

import com.atlassian.performance.tools.infrastructure.toSsh
import com.atlassian.performance.tools.sshubuntu.api.SshUbuntuContainer
import org.junit.Test

class ThreadDumpIT {
    @Test
    fun shouldGatherThreadDump() {
        SshUbuntuContainer().start().use { ssh ->
            val sshHost = ssh.toSsh()
            val jdk = OracleJDK()
            sshHost.newConnection().use { sshConnection ->
                jdk.install(sshConnection)
                sshConnection.execute("""echo "public class Test { public static void main(String[] args) { try { Thread.sleep(java.time.Duration.ofMinutes(1).toMillis()); } catch (InterruptedException e) { throw new RuntimeException(e); } }}" > Test.java """.trimIndent())
                sshConnection.execute("${jdk.use()} Test.java")
                val process = sshConnection.startProcess("${jdk.use()} Test.java")


                sshConnection.stopProcess(process)
            }
        }
    }
}