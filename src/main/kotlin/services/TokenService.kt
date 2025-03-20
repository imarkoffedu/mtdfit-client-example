package com.imarkoff.services

import com.imarkoff.schemas.DeviceDto
import com.imarkoff.utils.saveTokens
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

/**
 * Service for managing tokens and devices.
 */
class TokenService(override val client: HttpClient): IService {
    /** Refreshes the access token. */
    suspend fun refreshToken() {
        val response = client.post("token")
        saveTokens(client, response)
    }

    /** Gets the list of devices which can call refresh token API. */
    suspend fun getDevices(): Array<DeviceDto> {
        val response = client.get("token")
        return response.body()
    }
}