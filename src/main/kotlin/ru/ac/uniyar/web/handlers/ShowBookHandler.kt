package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.BookVM
import ru.ac.uniyar.models.Pager
import ru.ac.uniyar.web.lens.pageLens
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showBook(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getBook: GetBook
): HttpHandler = { request ->
    val user = userLens(request)
    val currentPage = pageLens(request)
    bookIdPathLens(request)?.let { id ->
        getBook.getFullData(id, currentPage, user?.login)
    }?.let { bookFullData ->
        val pager = Pager(currentPage, request.uri, bookFullData.chaptersNumber)
        Response(Status.OK).with(htmlView(request) of BookVM(pager, bookFullData))
    } ?: Response(Status.BAD_REQUEST)
}
