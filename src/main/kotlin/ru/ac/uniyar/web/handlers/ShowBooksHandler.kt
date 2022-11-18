package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetBooks
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.models.BooksVM
import ru.ac.uniyar.web.lens.authorIdLens
import ru.ac.uniyar.web.lens.bookNameLens
import ru.ac.uniyar.web.lens.genreIdLens
import ru.ac.uniyar.web.lens.pageLens

fun showBooks(
    htmlView: BiDiBodyLens<ViewModel>,
    getBooks: GetBooks,
    getAuthors: GetAuthors,
    getGenres: GetGenres
): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val name = bookNameLens(request)
    val authorId = authorIdLens(request)
    val genreId = genreIdLens(request)
    val authors = getAuthors.listAll()
    val genres = getGenres.listAll()
    val books = getBooks.list(currentPage, name, authorId, genreId)
    val viewModel = BooksVM(books, authors, genres, request, currentPage)
    Response(Status.OK).with(htmlView of viewModel)
}
