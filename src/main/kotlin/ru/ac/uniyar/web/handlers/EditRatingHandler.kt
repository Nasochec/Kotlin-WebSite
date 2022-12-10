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
import ru.ac.uniyar.domain.db.queries.GetRating
import ru.ac.uniyar.domain.db.queries.UpdateRatings
import ru.ac.uniyar.models.EditRatingVM
import ru.ac.uniyar.web.lens.path.neededAgePathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditRating(
    htmlView: ContextAwareViewRenderer,
    getRating: GetRating,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val neededAge = neededAgePathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val rating = getRating.get(neededAge) ?: return@handler Response(Status.BAD_REQUEST)
    Response(Status.OK).with(htmlView(request) of EditRatingVM(rating, form))
}

fun editRating(
    htmlView: ContextAwareViewRenderer,
    getRating: GetRating,
    updateRatings: UpdateRatings
): HttpHandler = handler@{ request ->
    val neededAgeLens = FormField.int().required("neededAge", "Заполните необходимый возраст")
    val formLens = Body.webForm(
        Validator.Feedback,
        neededAgeLens
    ).toLens()
    var form = formLens(request)
    val neededAge = neededAgePathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val rating = getRating.get(neededAge) ?: return@handler Response(Status.BAD_REQUEST)
    try {
        if (form.errors.isEmpty()) {
            if (neededAgeLens(form) < 0) {
                val newErrors = form.errors + Invalid(
                    neededAgeLens.meta.copy(
                        description = "Необходимый возраст не может быть отрицательным."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (rating.neededAge != neededAgeLens(form) && getRating.get(neededAgeLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    neededAgeLens.meta.copy(
                        description = "Рейтинг с таким необходимым возрастом уже существует."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showEditRating(htmlView, getRating, form).invoke(request)
            updateRatings.edit(rating.neededAge, neededAgeLens(form))
            Response(Status.FOUND).header("location", "/ratings")
        } else
            showEditRating(htmlView, getRating, form).invoke(request)
    } catch (_: LensFailure) {
        showEditRating(htmlView, getRating, form).invoke(request)
    }
}
