package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment

class AppConfig(private val environment:Environment) {
    val webConfig:WebConfig = WebConfig.fromEnvironment(environment)
    val databaseConfig:DatabaseConfig = DatabaseConfig.formEnvironment(environment)

    companion object {
        val appEnv = Environment.fromResource("/ru/ac/uniyar/config/app.properties") overrides
                Environment.JVM_PROPERTIES overrides
                Environment.ENV overrides
                WebConfig.defaultEnv overrides
                DatabaseConfig.defaultEnv

        fun readConfiguration(): AppConfig = AppConfig(appEnv)
    }
}