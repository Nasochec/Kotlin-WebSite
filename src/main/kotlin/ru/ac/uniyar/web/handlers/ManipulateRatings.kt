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
import ru.ac.uniyar.domain.db.queries.AddRating
import ru.ac.uniyar.domain.db.queries.EditRating
import ru.ac.uniyar.domain.db.queries.GetRating
import ru.ac.uniyar.models.AddRatingVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

class ManipulateRatings(
    private val htmlView: ContextAwareViewRenderer,
    private val addRating: AddRating,
    private val editRating: EditRating,
    private val getRating: GetRating
) {
    companion object {
        private val neededAgeLens = FormField.int().required("neededAge", "Необходимый возраст")
        private val formLens = Body.webForm(
            Validator.Feedback,
            neededAgeLens
        ).toLens()
    }

    private fun validate(_form: WebForm): WebForm {
        var form = _form
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
        return form
    }

    fun showAddRating(
        form: WebForm = WebForm()
    ): HttpHandler = handler@{ request ->
        Response(Status.OK).with(htmlView(request) of AddRatingVM(form))
    }

    fun addRating(): HttpHandler = handler@{ request ->
        var form = formLens(request)
        try {
            if (form.errors.isEmpty()) {
                form = validate(form)
                if (form.errors.isNotEmpty())
                    return@handler showAddRating(form).invoke(request)
                addRating.insert(neededAgeLens(form))
                Response(Status.FOUND).header("location", "/ratings")
            } else {
                return@handler showAddRating(form).invoke(request)
            }
        } catch (lf: LensFailure) {
            return@handler showAddRating(form).invoke(request)
        }
    }
}
