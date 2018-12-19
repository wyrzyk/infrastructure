package com.atlassian.performance.tools.infrastructure.api.database

import com.atlassian.performance.tools.ssh.api.Ssh
import com.atlassian.performance.tools.ssh.api.SshConnection
import java.nio.file.Path

interface DatabaseDeployment {
    fun setupIfRequired(databaseType: Database.Type): String

    fun <T> executeSshCommand(runnable:(ssh: SshConnection) -> T): T

    fun bind(dataBaseIp: String, jiraHomeIp: String, keyPath: Path)

    fun getBoundDatabaseIp(): String?

    fun getBoundJirahomeIp(): String?

    fun getBoundKeyPath(): Path?
}