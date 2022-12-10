package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.models.ErrorPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun errorRequestFilter(
    htmlView: ContextAwareViewRenderer
) = Filter { next: HttpHandler ->
    { request: Request ->
        val response = next(request)
        if (response.status.clientError && response.body.length == 0L || response.status.serverError) {
            var message = ""
            if (response.status == Status.FORBIDDEN)
                message = "Доступ запрещён."
            response.with(htmlView(request) of ErrorPageVM(request.uri, message))
        } else {
            response
        }
    }
}
