package com.atlassian.performance.tools.infrastructure.api.database

import com.atlassian.performance.tools.infrastructure.DockerImage
import com.atlassian.performance.tools.infrastructure.api.dataset.DatasetPackage
import com.atlassian.performance.tools.ssh.api.Ssh
import com.atlassian.performance.tools.ssh.api.SshConnection
import com.atlassian.performance.tools.ssh.api.SshHost
import java.lang.IllegalStateException
import java.nio.file.Path
import java.time.Duration

class DockerDatabaseDeployment(
        private val source: DatasetPackage
): DatabaseDeployment {

    private var boundDatabaseIp: String? = null;
    private var boundJiraHomeIp: String? = null;
    private var boundKeyPath: Path? = null;

    private fun getDockerImage(dockerImage: String) = DockerImage(
            name = dockerImage,
            pullTimeout = Duration.ofMinutes(5)
    )

    override fun setupIfRequired(databaseType: Database.Type): String {
        return executeSshCommand {
            val data = source.download(it)
            val port = databaseType.port
            val homePath = databaseType.homePath
            getDockerImage(databaseType.dockerImage).run(
                    ssh = it,
                    parameters = "-p $port:$port -v `realpath $data`:$homePath",
                    arguments = databaseType.startupArgs
            )
            data
        }
    }

    override fun <T> executeSshCommand(runnable: (ssh: SshConnection) -> T): T {
        checkIfBound()
        val databaseHost = SshHost(getBoundDatabaseIp()!!, "ubuntu", getBoundKeyPath()!!)
        val databaseSsh = Ssh(databaseHost, connectivityPatience = 4)
        return databaseSsh.newConnection().use(runnable)
    }

    private fun checkIfBound() {
        if (getBoundDatabaseIp() == null || getBoundJirahomeIp() == null || getBoundKeyPath() == null) {
            throw IllegalStateException("Instance is not bound.")
        }
    }

    override fun bind(dataBaseIp: String, jiraHomeIp: String, keyPath: Path) {
        boundDatabaseIp = dataBaseIp
        boundJiraHomeIp = jiraHomeIp
        boundKeyPath = keyPath
    }

    override fun getBoundDatabaseIp(): String? = boundDatabaseIp

    override fun getBoundJirahomeIp(): String? = boundJiraHomeIp

    override fun getBoundKeyPath(): Path? = boundKeyPath

}