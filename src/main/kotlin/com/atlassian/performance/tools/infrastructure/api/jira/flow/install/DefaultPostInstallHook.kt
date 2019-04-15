package com.atlassian.performance.tools.infrastructure.api.jira.flow.install

import com.atlassian.performance.tools.infrastructure.api.jira.JiraNodeConfig
import com.atlassian.performance.tools.infrastructure.api.jira.flow.JiraNodeFlow
import com.atlassian.performance.tools.infrastructure.api.jira.flow.server.UbuntuSysstat
import com.atlassian.performance.tools.infrastructure.jira.flow.install.ProfilerHook
import com.atlassian.performance.tools.infrastructure.jira.flow.install.SplunkForwarderHook
import com.atlassian.performance.tools.ssh.api.SshConnection

class DefaultPostInstallHook(
    private val config: JiraNodeConfig
) : InstalledJiraHook {

    override fun hook(
        ssh: SshConnection,
        jira: InstalledJira,
        flow: JiraNodeFlow
    ) {
        listOf(
            JiraHomeProperty(),
            DisabledAutoBackup(),
            JvmConfig(config),
            ProfilerHook(config.profiler),
            SplunkForwarderHook(config.splunkForwarder),
            UbuntuSysstat()
        ).forEach { it.hook(ssh, jira, flow) }
    }
}