package com.imarkoff

import com.imarkoff.client.ClientFactory
import com.imarkoff.client.TokenStorage
import com.imarkoff.views.DevicesView
import com.imarkoff.views.LoginView
import com.imarkoff.views.MeView
import kotlinx.coroutines.runBlocking

fun main() {
    val client = ClientFactory().createClient()
    val privateClient = ClientFactory().createPrivateClient()

    try {
        val refreshToken = TokenStorage.getInstance().getTokens()?.refreshToken

        if (refreshToken != null) {
            runBlocking {
                MeView(privateClient).call()
                DevicesView(privateClient).call()
            }
        }
        else {
            runBlocking { LoginView(client).call() }
        }
    }
    finally {
        client.close()
        privateClient.close()
    }
}