package com.atlassian.performance.tools.infrastructure.api.jira.flow

class TcpServer(
    val ip: String,
    val publicPort: Int,
    val privatePort: Int,
    val name: String
) {
    constructor(
        ip: String,
        port: Int,
        name: String
    ) : this(
        ip,
        port,
        port,
        name
    )
}