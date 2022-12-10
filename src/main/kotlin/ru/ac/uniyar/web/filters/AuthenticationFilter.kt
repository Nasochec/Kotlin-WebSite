package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.authorization.Permissions
import ru.ac.uniyar.domain.db.queries.GetUserRole
import ru.ac.uniyar.domain.entities.User

fun authenticationFilter(
    userLens: BiDiLens<Request, User?>,
    permissionLens: RequestContextLens<Permissions>,
    getUserRole: GetUserRole
) =
    Filter { next: HttpHandler ->
        { request: Request ->
            val req = userLens(request)?.let { user ->
                request.with(permissionLens of getUserRole.get(user.login))
            } ?: request.with(permissionLens of Permissions("GUEST"))
            next(req)
        }
    }
