package com.atlassian.performance.tools.infrastructure.api.jira.flow.start

import com.atlassian.performance.tools.infrastructure.api.jira.flow.JiraNodeFlow
import com.atlassian.performance.tools.infrastructure.api.jira.flow.install.InstalledJira
import com.atlassian.performance.tools.infrastructure.api.jira.flow.server.StartedJira
import com.atlassian.performance.tools.ssh.api.SshConnection

class HookedJiraStart(
    private val start: JiraStart
) : JiraStart {

    override fun start(
        ssh: SshConnection,
        installed: InstalledJira,
        flow: JiraNodeFlow
    ): StartedJira {
        flow.listPreStartHooks().forEach { it.hook(ssh, installed, flow) }
        val started = start.start(ssh, installed, flow)
        flow.listPostStartHooks().forEach { it.hook(ssh, started, flow) }
        return started
    }
}