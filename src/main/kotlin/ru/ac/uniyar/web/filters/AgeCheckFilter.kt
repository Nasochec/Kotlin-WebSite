package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.authorization.Permissions
import ru.ac.uniyar.domain.entities.User
import java.time.LocalDateTime

/****/
fun ageCheckFilter(
    userLens: BiDiLens<Request, User?>,
    canUse: (Permissions) -> Boolean,
) = Filter { next ->
    { request -> // TODO
        userLens(request)?.let { user ->
            if (user.birthDate.atStartOfDay().plusYears(1) >= LocalDateTime.now())
                next(request)
            else
                null
        } ?: Response(Status.FORBIDDEN)
    }
}
