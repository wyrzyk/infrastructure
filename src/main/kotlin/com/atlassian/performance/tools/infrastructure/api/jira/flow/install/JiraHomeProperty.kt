package com.atlassian.performance.tools.infrastructure.api.jira.flow.install

import com.atlassian.performance.tools.infrastructure.api.jira.flow.InstalledJira
import com.atlassian.performance.tools.infrastructure.api.jira.flow.report.ReportTrack
import com.atlassian.performance.tools.ssh.api.SshConnection

class JiraHomeProperty : PostInstallHook {

    override fun hook(
        ssh: SshConnection,
        jira: InstalledJira,
        track: ReportTrack
    ) {
        val properties = "${jira.installation}/atlassian-jira/WEB-INF/classes/jira-application.properties"
        ssh.execute("echo jira.home=`realpath ${jira.home}` > $properties")
    }
}
