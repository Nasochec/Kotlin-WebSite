package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.UpdateChapters
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.lens.path.chapterNumberPathLens

fun publishChapter(userLens: BiDiLens<Request, User?>, updateChapters: UpdateChapters, getBook: GetBook): HttpHandler = handler@{ request ->
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val number = chapterNumberPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val book = getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (book.authorLogin != user.login)return@handler Response(Status.FORBIDDEN)
    updateChapters.publish(bookId, number)
    Response(Status.FOUND).header("location", "/book/$bookId")
}

fun hideChapter(userLens: BiDiLens<Request, User?>, updateChapters: UpdateChapters, getBook: GetBook): HttpHandler = handler@{ request ->
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val number = chapterNumberPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val book = getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (book.authorLogin != user.login)return@handler Response(Status.FORBIDDEN)
    updateChapters.hide(bookId, number)
    Response(Status.FOUND).header("location", "/book/$bookId")
}
