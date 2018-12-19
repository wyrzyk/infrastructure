package com.atlassian.performance.tools.infrastructure.api.database

import com.atlassian.performance.tools.ssh.api.SshConnection
import java.net.URI

interface Database {
    fun getDeployment(): DatabaseDeployment

    fun start(jira: URI): String

    enum class Type(
        val port: String,
        val homePath: String,
        val startupArgs: String,
        val dockerImage: String
    ) {
        MYSQL("3306d", "/var/lib/mysql", "--skip-grant-tables", "mysql:5.6.38"),
        ORACLE("", "", "", "")
    }
}
