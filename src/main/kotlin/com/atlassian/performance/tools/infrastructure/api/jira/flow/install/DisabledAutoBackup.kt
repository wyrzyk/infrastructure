package com.atlassian.performance.tools.infrastructure.api.jira.flow.install

import com.atlassian.performance.tools.infrastructure.api.jira.flow.InstalledJira
import com.atlassian.performance.tools.infrastructure.api.jira.flow.report.ReportTrack
import com.atlassian.performance.tools.ssh.api.SshConnection

class DisabledAutoBackup : PostInstallHook {

    override fun hook(
        ssh: SshConnection,
        jira: InstalledJira,
        track: ReportTrack
    ) {
        ssh.execute("echo jira.autoexport=false > ${jira.home}/jira-config.properties")
    }
}
