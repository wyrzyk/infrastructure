package com.atlassian.performance.tools.infrastructure.api.database

import com.atlassian.performance.tools.infrastructure.api.os.Ubuntu
import com.atlassian.performance.tools.ssh.api.SshConnection
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.URI
import java.time.Duration
import java.time.Instant

/**
 * @param maxConnections MySQL `max_connections` parameter.
 */
class MySqlDatabase(
    private val databaseDeployment: DatabaseDeployment
) : Database {

    override fun getDeployment(): DatabaseDeployment {
        return databaseDeployment
    }

    private val logger: Logger = LogManager.getLogger(this::class.java)

    private val ubuntu = Ubuntu()

    override fun start(jira: URI): String {
        val location = databaseDeployment.setupIfRequired(Database.Type.MYSQL);
        return databaseDeployment.executeSshCommand {
            waitForMysql(it)
            it.execute("""mysql -h 127.0.0.1  -u root -e "UPDATE jiradb.propertystring SET propertyvalue = '$jira' WHERE id IN (select id from jiradb.propertyentry where property_key like '%baseurl%');" """)
            location
        }
    }

    private fun waitForMysql(ssh: SshConnection) {
        ubuntu.install(ssh, listOf("mysql-client"))
        val mysqlStart = Instant.now()
        while (!ssh.safeExecute("mysql -h 127.0.0.1 -u root -e 'select 1;'").isSuccessful()) {
            if (Instant.now() > mysqlStart + Duration.ofMinutes(15)) {
                throw RuntimeException("MySql didn't start in time")
            }
            logger.debug("Waiting for MySQL...")
            Thread.sleep(Duration.ofSeconds(10).toMillis())
        }
    }
}