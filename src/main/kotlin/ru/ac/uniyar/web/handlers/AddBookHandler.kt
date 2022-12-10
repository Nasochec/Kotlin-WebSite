package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.GetSimpleTables
import ru.ac.uniyar.domain.db.queries.AddBook
import ru.ac.uniyar.domain.db.tables.BOOK_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.AddBookVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAddBook(
    htmlView: ContextAwareViewRenderer,
    getSimpleTables: GetSimpleTables,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    val genres = getSimpleTables.getGenres.listAll()
    val formats = getSimpleTables.getFormats.listAll()
    val ratings = getSimpleTables.getRatings.listAll()
    Response(Status.OK).with(htmlView(request) of AddBookVM(form, genres, formats, ratings))
}

fun addBook(
    userLens: BiDiLens<Request, User?>,
    htmlView: ContextAwareViewRenderer,
    addBook: AddBook,
    getSimpleTables: GetSimpleTables
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните название книги")
    val ratingLens = FormField.int().required("rating", "Заполните возрастной рейтинг книги")
    val formatLens = FormField.nonEmptyString().required("format", "Заполните формат книги")
    val genreLens = FormField.nonEmptyString().required("genre", "Заполните жанр книги")
    val annotationLens = FormField.nonEmptyString().required("annotation", "Заполните аннотацию книги")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens,
        ratingLens,
        formatLens,
        genreLens,
        annotationLens
    ).toLens()
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    var form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            if (nameLens(form).length > BOOK_NAME_MAX_LENGTH) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(description = "Длина названия книги не должна превышать $BOOK_NAME_MAX_LENGTH символов.")
                )
                form = form.copy(errors = newErrors)
            }
            if (getSimpleTables.getGenre.get(genreLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    genreLens.meta.copy(
                        description = "Выбран некорректный жанр. Либо список жанров пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (getSimpleTables.getRating.get(ratingLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    ratingLens.meta.copy(
                        description = "Выбран некорректный возрастной рейтинг. Либо список возрастных рейтингов пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (getSimpleTables.getFormat.get(formatLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    formatLens.meta.copy(
                        description = "Выбран некорректный формат книги. Либо список форматов пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showAddBook(htmlView, getSimpleTables, form).invoke(request)
            val id = addBook.insert(
                nameLens(form),
                user.login,
                genreLens(form),
                ratingLens(form),
                formatLens(form),
                annotationLens(form)
            )
            Response(Status.FOUND).header("location", "/book/$id")
        } else {
            showAddBook(htmlView, getSimpleTables, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        showAddBook(htmlView, getSimpleTables, form).invoke(request)
    }
}
