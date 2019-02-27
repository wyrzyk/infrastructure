package com.atlassian.performance.tools.infrastructure.api.jvm

import com.atlassian.performance.tools.ssh.api.SshConnection

class ThreadDump(
    private val pid: Int,
    private val jdk: JavaDevelopmentKit
) {
    fun gather(connection: SshConnection): String? {
        val command = "${jdk.use()} jcmd $pid Thread.print"
        val result = connection.safeExecute(command)
        return if (result.isSuccessful()) {
            result.output
        } else {
            null
        }
    }
}