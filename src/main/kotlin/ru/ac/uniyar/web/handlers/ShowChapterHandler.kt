package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.db.queries.AddChapterBookmark
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.ChapterVM
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.lens.path.chapterNumberPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showChapter(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getChapter: GetChapter,
    addChapterBookmark: AddChapterBookmark
): HttpHandler = handler@{ request ->
    val bookId = bookIdPathLens(request)
    val number = chapterNumberPathLens(request)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN) // гости не могут смотреть главы
    if (bookId == null || number == null)
        return@handler Response(Status.BAD_REQUEST)
    getChapter.getFullData(bookId, number, user.login)?.let { chapterFullData ->
        addChapterBookmark.add(user.login, bookId, number)
        val viewModel = ChapterVM(chapterFullData)
        Response(Status.OK).with(htmlView(request) of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
