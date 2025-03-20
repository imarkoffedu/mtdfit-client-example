package com.imarkoff.utils

import com.imarkoff.client.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode

/**
 * Saves access/refresh tokens from HTTP response.
 */
suspend fun saveTokens(client: HttpClient, response: HttpResponse) {
    if (response.status.value !in listOf(HttpStatusCode.OK.value, HttpStatusCode.Created.value)) {
        println("Failed to login: ${response.status.value}")
        return
    }

    val accessToken = response.bodyAsText().trim { c -> c == '"' }
    val refreshToken = client.cookies(response.request.url).find { it.name == "refreshToken" }?.value
    TokenStorage.getInstance().storeTokens(accessToken, refreshToken)
}