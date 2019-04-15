package com.atlassian.performance.tools.infrastructure.api.jira.flow

import com.atlassian.performance.tools.infrastructure.api.jira.flow.install.InstalledJiraHook
import com.atlassian.performance.tools.infrastructure.api.jira.flow.server.TcpServerHook
import com.atlassian.performance.tools.infrastructure.api.jira.flow.report.Report
import com.atlassian.performance.tools.infrastructure.api.jira.flow.start.StartedJiraHook
import net.jcip.annotations.ThreadSafe
import java.util.concurrent.CopyOnWriteArrayList

@ThreadSafe
class JiraNodeFlow {

    private val tcpServerHooks: MutableList<TcpServerHook> = CopyOnWriteArrayList<TcpServerHook>()
    private val installedJiraHooks: MutableList<InstalledJiraHook> = CopyOnWriteArrayList<InstalledJiraHook>()
    private val preStartHooks: MutableList<InstalledJiraHook> = CopyOnWriteArrayList<InstalledJiraHook>()
    private val postStartHooks: MutableList<StartedJiraHook> = CopyOnWriteArrayList<StartedJiraHook>()
    val reports: MutableList<Report> = CopyOnWriteArrayList<Report>()

    fun hookPreInstall(
        hook: TcpServerHook
    ) {
        tcpServerHooks.add(hook)
    }

    fun listPreInstallHooks(): Iterable<TcpServerHook> = tcpServerHooks

    fun hookPostInstall(
        hook: InstalledJiraHook
    ) {
        installedJiraHooks.add(hook)
    }

    fun listPostInstallHooks(): Iterable<InstalledJiraHook> = installedJiraHooks

    fun hookPreStart(
        hook: InstalledJiraHook
    ) {
        preStartHooks.add(hook)
    }

    fun listPreStartHooks(): Iterable<InstalledJiraHook> = preStartHooks

    fun hookPostStart(
        hook: StartedJiraHook
    ) {
        postStartHooks.add(hook)
    }

    fun listPostStartHooks(): Iterable<StartedJiraHook> = postStartHooks
}