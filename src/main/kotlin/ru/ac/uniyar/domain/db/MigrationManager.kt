package ru.ac.uniyar.domain.db

import org.flywaydb.core.Flyway
import ru.ac.uniyar.config.DatabaseConfig

fun performMigrations(databaseConfig: DatabaseConfig) {
    val flyway = Flyway
        .configure()
        .locations("ru/ac/uniyar/domain/db/migrations")
        .validateMigrationNaming(true)
        .dataSource(databaseConfig.JDBCConnectionString, "sa", null)
        .load()
    flyway.migrate()
}
