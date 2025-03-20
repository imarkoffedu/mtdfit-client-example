package com.imarkoff.services

import io.ktor.client.HttpClient

/**
 * Base interface for all service classes that require an HTTP client.
 * All service implementations should have a client property.
 */
interface IService {
    val client: HttpClient
}