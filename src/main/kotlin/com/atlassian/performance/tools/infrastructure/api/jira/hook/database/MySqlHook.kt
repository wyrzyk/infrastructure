package com.atlassian.performance.tools.infrastructure.api.jira.hook.database

import com.atlassian.performance.tools.infrastructure.api.database.DatabaseIpConfig
import com.atlassian.performance.tools.infrastructure.api.database.MysqlConnector
import com.atlassian.performance.tools.infrastructure.api.dataset.HttpDatasetPackage
import com.atlassian.performance.tools.infrastructure.api.jira.hook.PreInstallHooks
import com.atlassian.performance.tools.infrastructure.api.jira.hook.TcpServer
import com.atlassian.performance.tools.infrastructure.api.jira.hook.server.PreInstallHook
import com.atlassian.performance.tools.infrastructure.api.os.Ubuntu
import com.atlassian.performance.tools.ssh.api.Ssh
import com.atlassian.performance.tools.ssh.api.SshConnection
import java.time.Duration
import java.time.Instant

class MySqlHook(
    private val mysql: Ssh,
    private val dataset: HttpDatasetPackage
) : PreInstallHook {

    override fun run(ssh: SshConnection, server: TcpServer, hooks: PreInstallHooks) {
        mysql.newConnection().use { mysqlSshConnection ->
            val datasetLocation = dataset.download(mysqlSshConnection)
            mysqlSshConnection.execute("mv $datasetLocation /var/lib/mysql")
            Ubuntu().install(mysqlSshConnection, listOf("mysql-server"))
            mysqlSshConnection.execute("sudo /etc/init.d/mysql start --skip-grant-tables")
            waitForMysql(mysqlSshConnection)
        }

        hooks.hook(MysqlConnector())
        hooks.hook(DatabaseIpConfig("localhost"))
    }

    private fun waitForMysql(ssh: SshConnection) {
        val mysqlStart = Instant.now()
        while (!ssh.safeExecute("mysql -u root -e 'select 1;'").isSuccessful()) {
            if (Instant.now() > mysqlStart + Duration.ofMinutes(1)) {
                throw Exception("MySql didn't start in time")
            }
            Thread.sleep(Duration.ofSeconds(1).toMillis())
        }
    }

}
