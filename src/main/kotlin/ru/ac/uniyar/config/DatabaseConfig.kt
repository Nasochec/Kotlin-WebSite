package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.string

class DatabaseConfig(val databaseHost: String, val databasePort: Int, val databaseName: String) {
    val JDBCConnectionString = "jdbc:h2:tcp://$databaseHost/$databaseName"

    companion object {
        val databasePortLens = EnvironmentKey.int().required("database.port", "Database web port")
        val databaseHostLens = EnvironmentKey.string().required("database.host", "Database web host")
        val databaseNameLens = EnvironmentKey.string().required("database.name", "Database name")
        fun formEnvironment(environment: Environment) = DatabaseConfig(
            databaseHostLens(environment),
            databasePortLens(environment),
            databaseNameLens(environment)
        )

        val defaultEnv = Environment.defaults(
            databaseHostLens of "localhost",
            databasePortLens of 8082,
            databaseNameLens of "database.h2"
        )
    }
}
