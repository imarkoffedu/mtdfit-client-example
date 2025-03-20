package com.imarkoff.views

import com.imarkoff.services.AuthService
import com.imarkoff.services.LoginRequest
import io.ktor.client.HttpClient

/** View to handle logging in. */
class LoginView(private val client: HttpClient): IView {
    override suspend fun call() {
        print("Enter your email: ")
        val email = readln()
        print("Enter your password: ")
        val password = readln()

        val authService = AuthService(client)
        authService.login(LoginRequest(email, password))

        println("Please rerun the application to see the changes.")
    }
}