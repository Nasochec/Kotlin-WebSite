package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.db.queries.CountBooks
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetBooks
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.models.BooksVM
import ru.ac.uniyar.models.Pager
import ru.ac.uniyar.web.lens.authorLoginLens
import ru.ac.uniyar.web.lens.bookNameLens
import ru.ac.uniyar.web.lens.genreNameLens
import ru.ac.uniyar.web.lens.pageLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showBooks(
    htmlView: ContextAwareViewRenderer,
    getBooks: GetBooks,
    countBooks: CountBooks,
    getAuthors: GetAuthors,
    getGenres: GetGenres
): HttpHandler = { request ->
    // Далее кажется что кода слишком много, а также много запросов.
    // Тем не менее это вынуждено. Надо применить к списку несколько фильтраций.
    // А также требуется получить данные из базы данных для заполнения comboBox-ов.
    // Эти запросы к бд являются логически не зависимыми (для разных) целей, поэтому одним запросом не обойтись.
    val currentPage = pageLens(request)
    val name = bookNameLens(request)
    val authorLogin = authorLoginLens(request)
    val genreName = genreNameLens(request)
    val authors = getAuthors.listAll()
    val genres = getGenres.listAll()
    val books = getBooks.list(currentPage, name, authorLogin, genreName)
    val booksCount = countBooks.countFiltered(name, authorLogin, genreName)
    val pager = Pager(currentPage, request.uri, booksCount)
    val viewModel = BooksVM(pager, books, authors, genres, request)
    Response(Status.OK).with(htmlView(request) of viewModel)
}
