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
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.domain.db.queries.UpdateGenres
import ru.ac.uniyar.domain.db.tables.GENRE_NAME_MAX_LENGTH
import ru.ac.uniyar.models.EditGenreVM
import ru.ac.uniyar.web.lens.path.genreNamePathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditGenre(
    htmlView: ContextAwareViewRenderer,
    getGenre: GetGenre,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val genreName = genreNamePathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val genre = getGenre.get(genreName) ?: return@handler Response(Status.BAD_REQUEST)
    Response(Status.OK).with(htmlView(request) of EditGenreVM(genre, form))
}

fun editGenre(
    htmlView: ContextAwareViewRenderer,
    getGenre: GetGenre,
    updateGenres: UpdateGenres
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните название жанра")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens
    ).toLens()
    var form = formLens(request)
    val genreName = genreNamePathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val genre = getGenre.get(genreName) ?: return@handler Response(Status.BAD_REQUEST)
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
            if (genre.name != nameLens(form) && getGenre.get(nameLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Жанр с таким названием уже существует."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showEditGenre(htmlView, getGenre, form).invoke(request)
            updateGenres.edit(genre.name, nameLens(form))
            Response(Status.FOUND).header("location", "/genres")
        } else
            return@handler showEditGenre(htmlView, getGenre, form).invoke(request)
    } catch (_: LensFailure) {
        return@handler showEditGenre(htmlView, getGenre, form).invoke(request)
    }
}
