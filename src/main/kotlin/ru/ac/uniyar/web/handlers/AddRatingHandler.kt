package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import ru.ac.uniyar.domain.db.queries.AddRating
import ru.ac.uniyar.domain.db.queries.GetRating
import ru.ac.uniyar.models.AddRatingVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAddRating(
    htmlView: ContextAwareViewRenderer,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    Response(Status.OK).with(htmlView(request) of AddRatingVM(form))
}

fun addRating(
    htmlView: ContextAwareViewRenderer,
    addRating: AddRating,
    getRating: GetRating
): HttpHandler = handler@{ request ->
    val neededAgeLens = FormField.int().required("neededAge", "Необходимый возраст")
    val formLens = Body.webForm(
        Validator.Feedback,
        neededAgeLens
    ).toLens()
    var form = formLens(request)
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
            if (getRating.get(neededAgeLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    neededAgeLens.meta.copy(
                        description = "Рейтинг с таким необходимым возрастом уже существует."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showAddRating(htmlView, form).invoke(request)
            addRating.insert(neededAgeLens(form))
            Response(Status.FOUND).header("location", "/ratings")
        } else {
            return@handler showAddRating(htmlView, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        return@handler showAddRating(htmlView, form).invoke(request)
    }
}
