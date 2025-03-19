package com.imarkoff.client

import com.imarkoff.services.AuthService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.headers
import io.ktor.http.Cookie
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.http.path
import kotlinx.coroutines.runBlocking

typealias ClientConfig = HttpClientConfig<*>.() -> Unit

/**
 * Factory for creating HTTP clients.
 * @property environment the client environment.
 */
class ClientFactory(
    private val environment: ClientEnvironment = ClientEnvironment()
) {
    /** Creates a new HTTP client */
    fun createClient() = HttpClient(CIO, clientProps)

    /** Same as [createClient] but with authentication. */
    fun createPrivateClient() =
        HttpClient(CIO, clientPrivateProps(createRefreshClient()))

    /** This one will send request with refresh token cookie. */
    private fun createRefreshClient() = HttpClient(CIO, refreshClientProps)

    private val clientProps: ClientConfig = {
        val (osName, osVersion, userAgent) = environment.getDeviceInfo()

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(UserAgent) {
            agent = userAgent
        }
        install(HttpCookies)
        defaultRequest {
            url {
                val url = environment.getURL()
                protocol = url.protocol
                host = url.host
                port = url.port
                path(url.encodedPath)
            }
            headers {
                append("Sec-CH-UA-Platform", osName)
                append("Sec-CH-UA-Platform-Version", osVersion)
            }
        }
    }

    private val refreshClientProps: ClientConfig = {
        clientProps()

        install(HttpCookies) {
            storage = runBlocking { createCookieStorage() }
        }
    }

    private fun clientPrivateProps(client: HttpClient): ClientConfig = {
        clientProps()

        install(Auth) {
            bearer {
                loadTokens {
                    TokenStorage.getInstance().getTokens()
                }
                refreshTokens {
                    val authService = AuthService(client)
                    authService.refreshToken()
                    TokenStorage.getInstance().getTokens()
                }
            }
        }
    }

    /** Creates a cookie storage with the current refresh token. */
    private suspend fun createCookieStorage(): AcceptAllCookiesStorage {
        val storage = AcceptAllCookiesStorage()

        val token = TokenStorage.getInstance().getTokens()?.refreshToken
        if (token != null) {
            storage.addCookie(
                environment.getURL().build(),
                Cookie(
                    name = "refreshToken",
                    value = token,
                    domain = environment.getHost(),
                    path = "/",
                    secure = environment.getProtocol() == URLProtocol.HTTPS,
                    httpOnly = true
                )
            )
        }

        return storage
    }

}