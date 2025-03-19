package com.imarkoff

import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.yaml.YamlConfigLoader
import java.io.File

/**
 * Loads the application configuration from the specified file.
 * AI generated ahh code.
 */
class ConfigurationLoader {
    fun load(): ApplicationConfig {
        val environment = System.getProperty("ktor.environment") ?: ""
        val configPath = when (environment) {
            "production" -> "application.prod.yaml"
            "development" -> "application.dev.yaml"
            "test" -> "application.test.yaml"
            else -> "application.yaml"
        }

        val configFile = ClassLoader.getSystemResource(configPath)?.toURI()?.path
            ?: File("src/main/resources/$configPath").absolutePath

        return YamlConfigLoader().load(configFile)
            ?: throw IllegalStateException("Failed to load configuration from $configPath")
    }
}