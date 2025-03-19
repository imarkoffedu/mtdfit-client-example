package com.imarkoff.client

import com.imarkoff.ConfigurationLoader
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.server.config.ApplicationConfig

/**
 * Environment configuration for the client.
 */
class ClientEnvironment {
    private val appConfig: ApplicationConfig by lazy { ConfigurationLoader().load() }
    private val config = appConfig.config(CLIENT_CONFIG)

    fun getHost(): String = config.property(ENV_HOST).getString()
    fun getPath(): String? = config.propertyOrNull(ENV_PATH)?.getString()
    fun getPort(): Int? = config.propertyOrNull(ENV_PORT)?.getString()?.toInt()

    fun getURL() = URLBuilder().apply {
        protocol = getProtocol()
        this.host = getHost()
        getPath()?.let { encodedPath = it }
        getPort()?.let { this.port = it }
    }

    fun getProtocol(): URLProtocol {
        val protocolEnv = config.propertyOrNull(ENV_PROTOCOL)?.getString()
        return when (protocolEnv?.lowercase()) {
            "http" -> URLProtocol.HTTP
            "https" -> URLProtocol.HTTPS
            else -> DEFAULT_PROTOCOL
        }
    }

    fun getDeviceInfo(): DeviceInfo {
        val osName = System.getProperty("os.name")
        val osVersion = System.getProperty("os.version")
        val userAgent = "User-Agent: Mozilla/5.0 ($osName $osVersion) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36"
        return DeviceInfo(osName, osVersion, userAgent)
    }

    companion object {
        private const val CLIENT_CONFIG = "ktor.client"
        private const val ENV_HOST = "host"
        private const val ENV_PATH = "path"
        private const val ENV_PORT = "port"
        private const val ENV_PROTOCOL = "protocol"

        private val DEFAULT_PROTOCOL = URLProtocol.HTTP
    }
}

data class DeviceInfo(val osName: String, val osVersion: String, val userAgent: String)