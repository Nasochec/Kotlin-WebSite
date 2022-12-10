package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiLens
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.string
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.UpdateChapters
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.lens.path.chapterNumberPathLens

fun hidePublishChapter(
    userLens: BiDiLens<Request, User?>,
    updateChapters: UpdateChapters,
    getBook: GetBook
): HttpHandler = handler@{ request ->
    val hidePublishLens = FormField.string().required("hidePublish")
    val formLens = Body.webForm(
        Validator.Feedback,
        hidePublishLens
    ).toLens()
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val number = chapterNumberPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            if (getBook.get(bookId)?.authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
            if (hidePublishLens(form) == "Скрыть")
                updateChapters.hide(bookId, number)
            else
                updateChapters.publish(bookId, number)
            Response(Status.FOUND).header("location", "/chapter/$bookId/$number")
        } else
            Response(Status.BAD_REQUEST)
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
