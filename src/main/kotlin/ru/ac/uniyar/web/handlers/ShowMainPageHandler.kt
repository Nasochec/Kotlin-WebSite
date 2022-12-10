package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.models.MainPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showMainPage(
    htmlView: ContextAwareViewRenderer
): HttpHandler = { request ->
    Response(Status.OK).with(htmlView(request) of MainPageVM())
}
