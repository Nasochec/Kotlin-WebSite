package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.authorization.Permissions
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
    permissionsLens: RequestContextLens<Permissions>,
    getBook: GetBook
): HttpHandler = { request ->
    val user = userLens(request)
    val permissions = permissionsLens(request)
    val currentPage = pageLens(request)
    bookIdPathLens(request)?.let { id ->
        getBook.get(id)
    }?.let { book ->
        val isOwner = (user?.login == book.authorLogin)
        val bookFullData = if (permissions.can_view_chapters)
            getBook.getFullData(book.id, isOwner, currentPage, user!!.login)!!
        else
            getBook.getFullDataWithoutChapters(book.id)!!
        val pager = Pager(currentPage, request.uri, bookFullData.chaptersNumber)
        Response(Status.OK).with(htmlView(request) of BookVM(pager, bookFullData, isOwner))
    } ?: Response(Status.BAD_REQUEST)
}
