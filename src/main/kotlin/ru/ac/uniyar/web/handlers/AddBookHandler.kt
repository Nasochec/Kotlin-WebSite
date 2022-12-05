package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.db.tables.BOOK_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.AddBookVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAddBook(
    htmlView: ContextAwareViewRenderer,
    operationHolder: OperationHolder,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    val genres = operationHolder.getGenres.listAll()
    val formats = operationHolder.getFormats.listAll()
    val ratings = operationHolder.getRatings.listAll()
    Response(Status.OK).with(htmlView(request) of AddBookVM(form, genres, formats, ratings))
}

fun addBook(
    userLens: BiDiLens<Request, User?>,
    htmlView: ContextAwareViewRenderer,
    operationHolder: OperationHolder
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Название книги")
    val ratingLens = FormField.int().required("rating", "Возрасной рейтинг книги")
    val formatLens = FormField.nonEmptyString().required("format", "Формат книги")
    val genreLens = FormField.nonEmptyString().required("genre", "Жанр книги")
    val annotationLens = FormField.nonEmptyString().required("annotation", "")
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
            if (operationHolder.getGenre.get(genreLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    genreLens.meta.copy(
                        description = "Выбран некорректный жанр. Либо список жанров пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (operationHolder.getRating.get(ratingLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    ratingLens.meta.copy(
                        description = "Выбран некорректный возрастной рейтинг. Либо список возрастных рейтингов пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (operationHolder.getFormat.get(formatLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    formatLens.meta.copy(
                        description = "Выбран некорректный формат книги. Либо список форматов пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showAddBook(htmlView, operationHolder, form).invoke(request)
            val id = operationHolder.addBook.insert(
                nameLens(form),
                user.login,
                genreLens(form),
                ratingLens(form),
                formatLens(form),
                annotationLens(form)
            )
            Response(Status.FOUND).header("location", "/book/$id")
        } else {
            showAddBook(htmlView, operationHolder, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        showAddBook(htmlView, operationHolder, form).invoke(request)
    }
}
