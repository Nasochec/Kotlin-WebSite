package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.AuthorVM

fun showAuthor(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val indexPath = Path.of("index")
//    val pageLens = Query.int().defaulted("page", 1)
//    var currentPage = pageLens(request)
//    if (currentPage <= 0) currentPage = 1
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
