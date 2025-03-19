package com.imarkoff.services

import com.imarkoff.client.TokenStorage
import com.imarkoff.schemas.ClientDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * Service for managing authentication APIs.
 */
class AuthService(private val client: HttpClient) {
    suspend fun login(dto: LoginRequest) {
        val response = client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        saveTokens(response)
    }

    suspend fun refreshToken() {
        val response = client.post("token")
        saveTokens(response)
    }

    /**
     * Gets the current user. Needs to be authenticated.
     */
    suspend fun getMe(): GetMeResponse {
        val response = client.get("auth/me")
        return response.body()
    }

    private suspend fun saveTokens(response: HttpResponse) {
        if (response.status.value !in listOf(HttpStatusCode.OK.value, HttpStatusCode.Created.value)) {
            println("Failed to login: ${response.status.value}")
            return
        }

        val accessToken = response.bodyAsText().trim { c -> c == '"' }
        val refreshToken = client.cookies(response.request.url).find { it.name == "refreshToken" }?.value
        TokenStorage.getInstance().storeTokens(accessToken, refreshToken)
    }
}

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class GetMeResponse(
    val entity: ClientDto,
    val isFinallyRegistered: Boolean = false
)