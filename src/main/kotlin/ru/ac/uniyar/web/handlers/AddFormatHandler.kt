package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import ru.ac.uniyar.domain.db.queries.AddFormat
import ru.ac.uniyar.domain.db.queries.GetFormat
import ru.ac.uniyar.domain.db.tables.FORMAT_NAME_MAX_LENGTH
import ru.ac.uniyar.models.AddFormatVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAddFormat(
    htmlView: ContextAwareViewRenderer,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    Response(Status.OK).with(htmlView(request) of AddFormatVM(form))
}

fun addFormat(
    htmlView: ContextAwareViewRenderer,
    addFormat: AddFormat,
    getFormat: GetFormat
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Название формата книги")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens
    ).toLens()
    var form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            if (nameLens(form).length > FORMAT_NAME_MAX_LENGTH) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Длина названия формата не должна превышать $FORMAT_NAME_MAX_LENGTH символов."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (getFormat.get(nameLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Формат с таким названием уже существует."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showAddFormat(htmlView, form).invoke(request)
            addFormat.insert(nameLens(form))
            Response(Status.FOUND).header("location", "/formats")
        } else {
            return@handler showAddFormat(htmlView, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        return@handler showAddFormat(htmlView, form).invoke(request)
    }
}
