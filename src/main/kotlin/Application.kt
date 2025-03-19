package com.imarkoff

import com.imarkoff.client.ClientFactory
import com.imarkoff.client.TokenStorage
import com.imarkoff.schemas.LoginDto
import com.imarkoff.services.AuthService
import kotlinx.coroutines.runBlocking

fun main() {
    val client = ClientFactory().createClient()
    val privateClient = ClientFactory().createPrivateClient()

    try {
        runBlocking {
            val authService = AuthService(privateClient)
            val refreshToken = TokenStorage.getInstance().getTokens()?.refreshToken

            if (refreshToken != null) {
                authService.getMe()
            }
            else {
                print("Enter your email: ")
                val email = readln()
                print("Enter your password: ")
                val password = readln()
                authService.login(LoginDto(email, password))
            }
        }
    }
    finally {
        client.close()
        privateClient.close()
    }
}