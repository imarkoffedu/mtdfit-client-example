package com.imarkoff

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath

fun main() {
    val (osName, osVersion, userAgentInfo) = getDeviceInfo()

    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        defaultRequest {
            url {
                protocol = getProtocol()
                host = System.getenv("ktor.client.host")
                encodedPath = System.getenv("ktor.client.path")
                port = System.getenv("ktor.client.port")?.toInt() ?: 8080
            }
            headers {
                append("Sec-CH-UA-Platform", osName)
                append("Sec-CH-UA-Platform-Version", osVersion)
                append(HttpHeaders.UserAgent, userAgentInfo)
            }
        }
    }

    client.close()
}

fun getProtocol(): URLProtocol {
    val protocolEnv = System.getenv("ktor.client.protocol")
    return when (protocolEnv?.lowercase()) {
        "http" -> URLProtocol.HTTP
        "https" -> URLProtocol.HTTPS
        else -> URLProtocol.HTTP
    }
}

data class DeviceInfo(val osName: String, val osVersion: String, val userAgent: String)

fun getDeviceInfo(): DeviceInfo {
    val osName = System.getProperty("os.name")
    val osVersion = System.getProperty("os.version")
    val userAgent =
        "User-Agent: Mozilla/5.0 ($osName $osVersion) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36"
    return DeviceInfo(osName, osVersion, userAgent)
}