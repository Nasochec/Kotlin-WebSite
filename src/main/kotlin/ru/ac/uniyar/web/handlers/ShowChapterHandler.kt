package ru.ac.uniyar.web.handlers

import org.http4k.core.*
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
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (bookId == null || number == null)
        return@handler Response(Status.BAD_REQUEST)
    getChapter.getFullData(bookId, number)?.let { chapter ->
        try {
            addChapterBookmark.add(user.login, bookId, number)
        } catch (_: Exception) {
        }
        val viewModel = ChapterVM(chapter)
        Response(Status.OK).with(htmlView(request) of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
