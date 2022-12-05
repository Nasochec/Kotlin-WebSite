package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import ru.ac.uniyar.models.MainPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showMainPage(
    htmlView: ContextAwareViewRenderer
): HttpHandler = { request ->
    Response(Status.OK).with(htmlView(request) of MainPageVM())
}
