package ru.ac.uniyar.web

import ru.ac.uniyar.config.WebConfig
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.ktorm.database.Database
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.web.lens.HTMLView
import ru.ac.uniyar.web.routers.app

fun getApp(database:Database,webConfig:WebConfig):Http4kServer {
    val operationHolder = OperationHolder(database)
    return app(operationHolder, HTMLView).asServer(Undertow(webConfig.webPort))
}
