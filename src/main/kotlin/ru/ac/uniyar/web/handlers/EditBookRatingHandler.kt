package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.GetRating
import ru.ac.uniyar.domain.db.queries.GetRatings
import ru.ac.uniyar.domain.db.queries.UpdateBooks
import ru.ac.uniyar.models.EditBookRatingVM
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditBookRating(
    htmlView: ContextAwareViewRenderer,
    getBook: GetBook,
    getRatings: GetRatings,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val book = getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    val ratings = getRatings.listAll()
    Response(Status.OK).with(htmlView(request) of EditBookRatingVM(book, ratings, form))
}

fun editBookRating(
    htmlView: ContextAwareViewRenderer,
    getBook: GetBook,
    getRating: GetRating,
    getRatings: GetRatings,
    updateBooks: UpdateBooks
): HttpHandler = handler@{ request ->
    val ratingLens = FormField.int().required("rating", "Заполните возрастной рейтинг книги")
    val formLens = Body.webForm(
        Validator.Feedback,
        ratingLens,
    ).toLens()
    var form = formLens(request)
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    try {
        if (form.errors.isEmpty()) {
            if (getRating.get(ratingLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    ratingLens.meta.copy(
                        description = "Выбран некорректный возрастной рейтинг. Либо список возрастных рейтингов пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                showEditBookRating(htmlView, getBook, getRatings, form).invoke(request)
            updateBooks.editRatingAdmin(bookId, ratingLens(form))
            Response(Status.FOUND).header("location", "/book/$bookId")
        } else
            showEditBookRating(htmlView, getBook, getRatings, form).invoke(request)
    } catch (_: LensFailure) {
        showEditBookRating(htmlView, getBook, getRatings, form).invoke(request)
    }
}
