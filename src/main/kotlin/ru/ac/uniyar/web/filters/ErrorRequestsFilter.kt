package ru.ac.uniyar.web.filters

import org.http4k.core.*
import ru.ac.uniyar.models.ErrorPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun errorRequestFilter(
    htmlView: ContextAwareViewRenderer
) = Filter { next: HttpHandler ->
    { request: Request ->
        val response = next(request)
        var message = ""
        if (response.status.clientError && response.body.length == 0L || response.status.serverError) {
            if (response.status == Status.FORBIDDEN)
                message = "Доступ запрещён."
            response.with(htmlView(request) of ErrorPageVM(request.uri, message))
        } else {
            response
        }
    }
}
