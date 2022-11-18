package ru.ac.uniyar

import H2DatabaseManager
import org.flywaydb.core.api.FlywayException
import ru.ac.uniyar.config.AppConfig
import ru.ac.uniyar.domain.db.connectToDatabase
import ru.ac.uniyar.domain.db.performMigrations
import ru.ac.uniyar.web.getApp

fun main() {
    val appConfig = AppConfig.readConfiguration()
    val h2DatabaseManager = H2DatabaseManager(appConfig.databaseConfig.databasePort).initialize()
    try {
        performMigrations(appConfig.databaseConfig)
    } catch (flywayEx: FlywayException) {
        System.err.println("Миграция завершена с ошибкой:" + flywayEx.localizedMessage)
        h2DatabaseManager.stopServers()
        return
    }
    val database = connectToDatabase(appConfig.databaseConfig)
    val server = getApp(database, appConfig.webConfig).start()
    println("Сервер доступен по адресу http://localhost:" + server.port())
    println("Веб-интерфейс базы данных доступен по адресу http://localhost:${appConfig.databaseConfig.databasePort}")
    println("Введите любую строку, чтобы завершить работу приложения")
    readlnOrNull()
    server.stop()
    h2DatabaseManager.stopServers()
}
