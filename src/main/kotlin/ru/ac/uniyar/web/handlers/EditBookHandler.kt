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
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.authorization.Permissions
import ru.ac.uniyar.domain.db.GetSimpleTables
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.UpdateBooks
import ru.ac.uniyar.domain.db.tables.BOOK_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.EditBookVM
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditBook(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getBook: GetBook,
    getSimpleTables: GetSimpleTables,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val book = getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (book.authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
    Response(Status.OK).with(
        htmlView(request) of
            EditBookVM(
                book,
                getSimpleTables.getGenres.listAll(),
                getSimpleTables.getRatings.listAll(),
                getSimpleTables.getFormats.listAll(),
                form
            )
    )
}

fun editBook(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    permissionsLens: RequestContextLens<Permissions>,
    getBook: GetBook,
    getSimpleTables: GetSimpleTables,
    updateBooks: UpdateBooks
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните название книги")
    val ratingLens = FormField.int().optional("rating", "Заполните возрастной рейтинг книги")
    val formatLens = FormField.nonEmptyString().required("format", "Заполните формат книги")
    val genreLens = FormField.nonEmptyString().required("genre", "Заполните жанр книги")
    val annotationLens = FormField.nonEmptyString().required("annotation", "Заполните аннотация книги")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens,
        ratingLens,
        formatLens,
        genreLens,
        annotationLens
    ).toLens()
    var form = formLens(request)
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val book = getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (book.authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
    val permissions = permissionsLens(request)
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
            if (getSimpleTables.getFormat.get(formatLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    formatLens.meta.copy(
                        description = "Выбран некорректный формат книги. Либо список форматов пуст, обратитесь к администратору" +
                            " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (ratingLens(form) == null && (!book.neededAgeSetByAdmin || permissions.can_change_ratings)) {
                val newErrors = form.errors + Invalid(
                    ratingLens.meta
                )
                form = form.copy(errors = newErrors)
            }
            if (ratingLens(form) != null)
                if (getSimpleTables.getRating.get(ratingLens(form)!!) == null) {
                    val newErrors = form.errors + Invalid(
                        ratingLens.meta.copy(
                            description = "Выбран некорректный возрастной рейтинг. Либо список возрастных рейтингов пуст, обратитесь к администратору" +
                                " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                        )
                    )
                    form = form.copy(errors = newErrors)
                }
            if (form.errors.isNotEmpty())
                showEditBook(htmlView, userLens, getBook, getSimpleTables, form).invoke(request)
            updateBooks.edit(
                bookId, nameLens(form),
                when (!book.neededAgeSetByAdmin || permissions.can_change_ratings) {
                    false -> null
                    true -> ratingLens(form)
                },
                genreLens(form), formatLens(form)
            )
            Response(Status.FOUND).header("location", "/book/$bookId")
        } else
            showEditBook(htmlView, userLens, getBook, getSimpleTables, form).invoke(request)
    } catch (_: LensFailure) {
        showEditBook(htmlView, userLens, getBook, getSimpleTables, form).invoke(request)
    }
}
