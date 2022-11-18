package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.models.ChapterVM

fun showChapter(htmlView: BiDiBodyLens<ViewModel>, getChapter: GetChapter): HttpHandler = handler@{ request ->
    val bookIdPath = Path.of("bookId")
    val idPath = Path.of("id")
    val bookId = bookIdPath(request).toIntOrNull()
    val id = idPath(request).toIntOrNull()
    if (bookId == null || id == null)
        return@handler Response(Status.BAD_REQUEST)
    getChapter.getFullData(bookId, id)?.let { chapter ->
        val viewModel = ChapterVM(chapter)
        Response(Status.OK).with(htmlView of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
