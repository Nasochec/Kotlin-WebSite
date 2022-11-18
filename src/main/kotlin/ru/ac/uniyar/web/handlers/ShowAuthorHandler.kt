package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.models.AuthorVM

fun showAuthor(htmlView: BiDiBodyLens<ViewModel>, getAuthor: GetAuthor): HttpHandler = { request ->
    val indexPath = Path.of("index")
    indexPath(request).toIntOrNull()?.let {
        getAuthor.getFullData(it)
    }?.let { authorData ->
        Response(Status.OK).with(htmlView of AuthorVM(authorData))
    } ?: Response(Status.BAD_REQUEST)
}
