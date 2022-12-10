package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.models.AuthorVM
import ru.ac.uniyar.web.lens.path.authorLoginPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAuthor(
    htmlView: ContextAwareViewRenderer,
    getAuthor: GetAuthor
): HttpHandler = { request ->
    authorLoginPathLens(request)?.let { login ->
        getAuthor.getFullData(login)
    }?.let { authorData ->
        Response(Status.OK).with(htmlView(request) of AuthorVM(authorData))
    } ?: Response(Status.BAD_REQUEST)
}
