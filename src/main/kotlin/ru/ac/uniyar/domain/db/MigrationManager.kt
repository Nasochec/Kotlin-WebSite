package ru.ac.uniyar.domain.db

import org.flywaydb.core.Flyway

fun performMigrations() {
    val flyway = Flyway
        .configure()
        .locations("ru/ac/uniyar/domain/db/migrations")
        .validateMigrationNaming(true)
        .dataSource(H2DatabaseManager.JDBC_CONNECTION, "sa", null)
        .load()
    flyway.migrate()
}
