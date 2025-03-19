package com.imarkoff.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.path

/**
 * Factory for creating HTTP clients.
 * @property environment the client environment.
 */
class ClientFactory(
    private val environment: ClientEnvironment = ClientEnvironment()
) {
    /** Creates a new HTTP client */
    fun createClient() = HttpClient(CIO, clientProps)

    /** Same as @see createClient but with authentication. */
    fun createPrivateClient() = HttpClient(CIO, clientPrivateProps)

    private val clientProps: HttpClientConfig<*>.() -> Unit = {
        val (osName, osVersion) = environment.getDeviceInfo()

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(UserAgent) {
            agent = "ktor-client"
        }
        defaultRequest {
            url {
                protocol = environment.getProtocol()
                host = environment.getHost()
                environment.getPath() ?.let { path(it) }
                environment.getPort() ?.let { port = it }
            }
            headers {
                append("Sec-CH-UA-Platform", osName)
                append("Sec-CH-UA-Platform-Version", osVersion)
            }
        }
    }

    private val clientPrivateProps: HttpClientConfig<*>.() -> Unit = {
        clientProps()

        install(Auth) {
            bearer {
                loadTokens {
                    val token = System.getenv("ktor.client.token")
                    if (token != null) {
                        null
                    } else {
                        null
                    }
                }
            }
        }
    }
}