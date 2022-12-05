package ru.ac.uniyar.web.filters

import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.db.queries.GetUserByToken
import ru.ac.uniyar.domain.entities.User

fun authorizationFilter(userLens: BiDiLens<Request, User?>, getUserByToken: GetUserByToken) =
    Filter { next: HttpHandler ->
        { request: Request ->
            val req = request.cookie("auth_token")?.value?.let { token ->
                getUserByToken.get(token)
            }?.let { user ->
                request.with(userLens of user)
            } ?: request
            next(req)
        }
    }
