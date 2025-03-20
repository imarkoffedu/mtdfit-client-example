package com.imarkoff.views

import com.imarkoff.schemas.DeviceDto
import com.imarkoff.services.TokenService
import com.imarkoff.utils.parseUTC
import io.ktor.client.HttpClient
import java.time.LocalDateTime

/**
 * View for displaying the list of devices.
 */
class DevicesView(private val client: HttpClient): IView {
    override suspend fun call() {
        val tokenService = TokenService(client)
        val devices = tokenService.getDevices()

        println("Here is the list of devices connected to your account:")

        devices.forEach {
            println("----------")
            PrintDevice(it)
        }
    }

    private class PrintDevice(private val device: DeviceDto) {
        init {
            if (isActive()) println("Current device.")

            device.deviceName?.let { println("Device name: $it") }
            if (!device.os.isNullOrBlank()) println("OS: ${device.os}")
            println("IP: ${device.ipAddress}")
            println("User agent: ${device.userAgent}")
            println("Last used: ${device.lastUsed}")
        }

        private fun isActive(): Boolean {
            val currentOSName = System.getProperty("os.name")
            val currentOSVersion = System.getProperty("os.version")
            val currentOS = "$currentOSName $currentOSVersion"

            val lastUsedDate = device.lastUsed.parseUTC()
            val now = LocalDateTime.now()

            return (
                device.os == currentOS &&
                lastUsedDate.plusMinutes(15) > now
            )
        }
    }
}