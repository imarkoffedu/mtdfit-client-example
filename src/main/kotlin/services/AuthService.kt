package com.imarkoff.services

import com.imarkoff.schemas.ClientDto
import com.imarkoff.utils.saveTokens
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * Service for managing authentication APIs.
 */
class AuthService(override val client: HttpClient): IService {
    suspend fun login(dto: LoginRequest) {
        val response = client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        saveTokens(client, response)
    }

    /**
     * Gets the current user. Needs to be authenticated.
     */
    suspend fun getMe(): GetMeResponse {
        val response = client.get("auth/me")
        return response.body()
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