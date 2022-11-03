package ru.ac.uniyar

import H2DatabaseManager
import org.flywaydb.core.api.FlywayException
import org.http4k.server.Undertow
import org.http4k.server.asServer
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.db.connectToDatabase
import ru.ac.uniyar.domain.db.performMigrations
import ru.ac.uniyar.web.lens.HTMLView
import ru.ac.uniyar.web.routers.app

fun main() {
    val h2DatabaseManager = H2DatabaseManager().initialize()
    try {
        performMigrations()
    } catch (flywayEx: FlywayException) {
        System.err.println("Миграция завершена с ошибкой:" + flywayEx.localizedMessage)
        h2DatabaseManager.stopServers()
        return
    }
    val database = connectToDatabase()
    val operationHolder: OperationHolder = OperationHolder(database)

    val server = app(operationHolder, HTMLView).asServer(Undertow(9000)).start()
    println("Сервер доступен по адресу http://localhost:" + server.port())
    println("Веб-интерфейс базы данных доступен по адресу http://localhost:${H2DatabaseManager.WEB_PORT}")
    println("Введите любую строку, чтобы завершить работу приложения")
    readlnOrNull()
    server.stop()
    h2DatabaseManager.stopServers()
}
