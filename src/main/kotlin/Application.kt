package com.imarkoff

import com.imarkoff.client.ClientFactory

fun main() {
    val client = ClientFactory().createClient()
    val privateClient = ClientFactory().createPrivateClient()

    client.close()
    privateClient.close()
}