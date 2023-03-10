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
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.queries.AddGenre
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.domain.db.tables.GENRE_NAME_MAX_LENGTH
import ru.ac.uniyar.models.AddGenreVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAddGenre(
    htmlView: ContextAwareViewRenderer,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    Response(Status.OK).with(htmlView(request) of AddGenreVM(form))
}

fun addGenre(
    htmlView: ContextAwareViewRenderer,
    addGenre: AddGenre,
    getGenre: GetGenre
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните название жанра")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens
    ).toLens()
    var form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            if (nameLens(form).length > GENRE_NAME_MAX_LENGTH) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Длина названия жанра не должна превышать $GENRE_NAME_MAX_LENGTH символов."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (getGenre.get(nameLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Жанр с таким названием уже существует."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showAddGenre(htmlView, form).invoke(request)
            addGenre.insert(nameLens(form))
            Response(Status.FOUND).header("location", "/genres")
        } else {
            return@handler showAddGenre(htmlView, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        return@handler showAddGenre(htmlView, form).invoke(request)
    }
}
