package com.atlassian.performance.tools.infrastructure.api.dataset

import com.atlassian.performance.tools.infrastructure.Ls
import com.atlassian.performance.tools.infrastructure.toSsh
import com.atlassian.performance.tools.ssh.api.Ssh
import com.atlassian.performance.tools.sshubuntu.api.SshUbuntuContainer
import org.assertj.core.api.Assertions
import org.junit.Ignore
import org.junit.Test
import java.net.URI
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class HttpDatasetPackageIT {

    @Test
    @Ignore
    fun shouldDownloadDataset() {
        val dataset = HttpDatasetPackage(
            uri = URI("https://s3-eu-west-1.amazonaws.com/jpt-custom-datasets-storage-a008820-datasetbucket-1sjxdtrv5hdhj/af4c7d3b-925c-464c-ab13-79f615158316/database.tar.bz2"),
            downloadTimeout = Duration.ofMinutes(2)
        )

        val filesInDataset = SshUbuntuContainer().start().use { sshUbuntu ->
            val ssh = sshUbuntu.toSsh()
            return@use RandomFilesGenerator(ssh).start().use {
                ssh.newConnection().use { connection ->
                    val unpackedPath = dataset.download(connection)
                    Ls().execute(connection, unpackedPath)
                }
            }
        }

        Assertions
            .assertThat(filesInDataset)
            .containsExactlyInAnyOrder(
                "auto.cnf",
                "ib_logfile0",
                "ib_logfile1",
                "ibdata1",
                "jiradb",
                "mysql",
                "performance_schema"
            )
    }

    private class RandomFilesGenerator(private val ssh: Ssh) {
        fun start(): AutoCloseable {
            val generator = object : AutoCloseable {
                private val executor = Executors.newSingleThreadExecutor()
                private val createNewFiles = AtomicBoolean(true)

                fun start() {
                    executor.execute {
                        while (createNewFiles.get()) {
                            ssh.newConnection().use {
                                it.safeExecute(
                                    cmd = "touch ${UUID.randomUUID()}"
                                )
                            }
                            Thread.sleep(20)
                        }
                    }
                }

                override fun close() {
                    createNewFiles.set(false)
                    executor.shutdown()
                }
            }
            generator.start()
            return generator
        }
    }
}
