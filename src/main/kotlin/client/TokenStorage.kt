package com.imarkoff.client

import io.ktor.client.plugins.auth.providers.BearerTokens
import java.io.File

/**
 * Singleton storage for JWT's.
 */
class TokenStorage private constructor() {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    // знаю що таке добро треба ще й шифрувати від небажаних гостей
    // знаю для Android такий спосіб не підійде
    private val refreshFile = File(System.getProperty("user.home"), ".ktor-jwt-refresh")

    init { loadTokens() }

    fun storeTokens(access: String, refresh: String? = null) {
        accessToken = access
        refreshToken?.let { refreshToken = refresh }
        refreshFile.writeText(refresh ?: "")
    }

    fun loadTokens() {
        if (refreshFile.exists())
            refreshToken = refreshFile.readText()
    }

    fun getTokens(): BearerTokens? {
        return if (accessToken == null && refreshToken == null) null
        else BearerTokens(accessToken ?: "", refreshToken)
    }

    fun clearTokens() {
        accessToken = null
        refreshToken = null
        refreshFile.delete()
    }

    companion object {
        private val instance = TokenStorage()

        /** Gets the singleton instance of the token storage. */
        fun getInstance() = instance
    }
}