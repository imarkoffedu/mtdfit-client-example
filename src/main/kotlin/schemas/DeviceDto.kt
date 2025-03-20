package com.imarkoff.schemas

import com.imarkoff.utils.DateString
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: String, // uuid
    val userAgent: String,
    val ipAddress: String,
    val deviceName: String?,
    val os: String?,
    val dateCreated: DateString,
    val lastUsed: DateString
)
