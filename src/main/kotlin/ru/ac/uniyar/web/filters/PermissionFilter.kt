package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.authorization.Permissions

fun permissionFilter(
    permissionLens: RequestContextLens<Permissions>,
    canUse: (Permissions) -> Boolean,
) = Filter { next ->
    { request ->
        if (permissionLens(request).let(canUse))
            next(request)
        else
            Response(Status.FORBIDDEN)
    }
}
