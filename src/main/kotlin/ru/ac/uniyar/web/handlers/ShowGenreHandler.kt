package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.GenreVM

fun showGenre(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val indexPath = Path.of("index")
    indexPath(request).toIntOrNull()?.let { id ->
        operationHolder.getGenre.get(id)
    }?.let { genre ->
        val books = operationHolder.getGenreBooks.list(genre.id)
        val viewModel = GenreVM(genre, books)
        Response(Status.OK).with(htmlView of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
