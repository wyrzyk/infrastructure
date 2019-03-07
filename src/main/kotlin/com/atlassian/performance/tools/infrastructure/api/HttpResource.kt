package com.atlassian.performance.tools.infrastructure.api

import com.atlassian.performance.tools.infrastructure.api.os.Ubuntu
import com.atlassian.performance.tools.ssh.api.SshConnection
import java.net.URI
import java.time.Duration

/**
 * TODO
 */
class HttpResource(
    private val uri: URI
) {

    /**
     * TODO
     */
    fun download(
        ssh: SshConnection,
        destination: String
    ) {
        download(
            ssh,
            destination,
            Duration.ofMinutes(2)
        )
    }

    /**
     * TODO
     */
    fun download(
        ssh: SshConnection,
        destination: String,
        timeout: Duration
    ) {
        Ubuntu().install(ssh, listOf("lftp"))
        ssh.execute(
            """lftp -c 'pget -n 64 -c "$uri" -o $destination'""",
            timeout
        )
    }
}
