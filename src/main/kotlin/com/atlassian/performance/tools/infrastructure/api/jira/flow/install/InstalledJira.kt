package com.atlassian.performance.tools.infrastructure.api.jira.flow.install

import com.atlassian.performance.tools.infrastructure.api.jvm.JavaDevelopmentKit

class InstalledJira(
    val home: String,
    val installation: String,
    val name: String,
    val jdk: JavaDevelopmentKit
)
