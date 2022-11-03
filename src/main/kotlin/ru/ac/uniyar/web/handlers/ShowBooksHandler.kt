package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.BooksVM
import ru.ac.uniyar.web.lens.authorIdLens
import ru.ac.uniyar.web.lens.bookNameLens
import ru.ac.uniyar.web.lens.genreIdLens
import ru.ac.uniyar.web.lens.pageLens

fun showBooks(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val name = bookNameLens(request)
    val authorId = authorIdLens(request)
    val genreId = genreIdLens(request)
    val authors = operationHolder.getAuthors.listAll()
    val genres = operationHolder.getGenres.listAll()
    val books = operationHolder.getBooks.list(currentPage, name, authorId, genreId)
    val viewModel = BooksVM(books, authors, genres, request, currentPage)
    Response(Status.OK).with(htmlView of viewModel)
}
