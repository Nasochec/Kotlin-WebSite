package ru.ac.uniyar.web

import org.http4k.core.ContentType
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.ktorm.database.Database
import ru.ac.uniyar.authorization.JwtTools
import ru.ac.uniyar.authorization.Permissions
import ru.ac.uniyar.config.SaltConfig
import ru.ac.uniyar.config.WebConfig
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.web.filters.authenticationFilter
import ru.ac.uniyar.web.filters.authorizationFilter
import ru.ac.uniyar.web.routers.app
import ru.ac.uniyar.web.templates.ContextAwarePebbleTemplates
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun getApp(database: Database, webConfig: WebConfig, saltConfig: SaltConfig): Http4kServer {
    val contexts = RequestContexts()
    val userContexts: RequestContextLens<User?> = RequestContextKey.optional<User>(contexts, "user")
    val permissionsContexts: RequestContextLens<Permissions> =
        RequestContextKey.required<Permissions>(contexts, "permissions")
    val jwtTools = JwtTools(saltConfig.salt, "pupa")
    val operationHolder = OperationHolder(database, saltConfig.salt, jwtTools)
    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRenderer.withContentType(renderer, ContentType.TEXT_HTML)
    val htmlViewWithContext = htmlView
        .associateContextLens("user", userContexts)
        .associateContextLens("permissions", permissionsContexts)

    return ServerFilters
        .InitialiseRequestContext(contexts)
        .then(authorizationFilter(userContexts, operationHolder.getUserByToken))
        .then(authenticationFilter(userContexts, permissionsContexts, operationHolder.getUserRole))
        .then(app(operationHolder, htmlViewWithContext, jwtTools, userContexts, permissionsContexts))
        .asServer(Undertow(webConfig.webPort))
}
