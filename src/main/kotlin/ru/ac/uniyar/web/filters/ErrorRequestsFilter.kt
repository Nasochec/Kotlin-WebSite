package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.models.ErrorPageVM

fun errorRequestFilter(htmlView: BiDiBodyLens<ViewModel>) = Filter { next: HttpHandler ->
    { request: Request ->
        val response = next(request)
        if (response.status.clientError && response.body.length == 0L || response.status.serverError) {
            response.with(htmlView of ErrorPageVM(request.uri))
        } else {
            response
        }
    }
}
