package ru.ac.uniyar

import H2DatabaseManager
import ru.ac.uniyar.config.AppConfig
import ru.ac.uniyar.config.WebConfig
import org.flywaydb.core.api.FlywayException
import ru.ac.uniyar.domain.db.connectToDatabase
import ru.ac.uniyar.domain.db.performMigrations
import ru.ac.uniyar.web.getApp

fun main() {
    val appConfig =AppConfig.readConfiguration()
    val h2DatabaseManager = H2DatabaseManager().initialize()
    try {
        performMigrations(appConfig.databaseConfig)
    } catch (flywayEx: FlywayException) {
        System.err.println("Миграция завершена с ошибкой:" + flywayEx.localizedMessage)
        h2DatabaseManager.stopServers()
        return
    }
    val database = connectToDatabase(appConfig.databaseConfig)
    val webConfig: WebConfig = appConfig.webConfig
    val server = getApp(database,webConfig).start()
    println("Сервер доступен по адресу http://localhost:" + server.port())
    println("Веб-интерфейс базы данных доступен по адресу http://localhost:${H2DatabaseManager.WEB_PORT}")
    println("Введите любую строку, чтобы завершить работу приложения")
    readlnOrNull()
    server.stop()
    h2DatabaseManager.stopServers()
}
