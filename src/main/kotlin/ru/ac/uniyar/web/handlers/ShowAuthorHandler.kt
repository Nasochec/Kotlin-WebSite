package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.with
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.AuthorVM

fun showAuthor(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val indexPath = Path.of("index")
    indexPath(request).toIntOrNull()?.let {
        operationHolder.getAuthor.get(it)
    }?.let {
        val books = operationHolder.getAuthorBooks.list(it.id)
        val genres = operationHolder.getAuthorGenre.list(it.id)
        val lastActivity = operationHolder.getAuthorLastActivity.get((it.id))
        val viewModel = AuthorVM(it, books, genres, lastActivity)
        Response(Status.OK).with(htmlView of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
