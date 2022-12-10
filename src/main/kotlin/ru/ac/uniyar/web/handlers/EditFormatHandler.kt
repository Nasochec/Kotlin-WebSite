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
import ru.ac.uniyar.domain.db.queries.GetFormat
import ru.ac.uniyar.domain.db.queries.UpdateFormats
import ru.ac.uniyar.domain.db.tables.FORMAT_NAME_MAX_LENGTH
import ru.ac.uniyar.models.EditFormatVM
import ru.ac.uniyar.web.lens.path.formatNamePathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditFormat(
    htmlView: ContextAwareViewRenderer,
    getFormat: GetFormat,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val name = formatNamePathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val format = getFormat.get(name) ?: return@handler Response(Status.BAD_REQUEST)
    Response(Status.OK).with(htmlView(request) of EditFormatVM(format, form))
}

fun editFormat(
    htmlView: ContextAwareViewRenderer,
    getFormat: GetFormat,
    updateFormats: UpdateFormats
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните название формата книги")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens
    ).toLens()
    var form = formLens(request)
    val name = formatNamePathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val format = getFormat.get(name) ?: return@handler Response(Status.BAD_REQUEST)
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
            if (format.name != nameLens(form) && getFormat.get(nameLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Формат с таким названием уже существует."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showEditFormat(htmlView, getFormat, form).invoke(request)
            updateFormats.edit(format.name, nameLens(form))
            Response(Status.FOUND).header("location", "/formats")
        } else
            showEditFormat(htmlView, getFormat, form).invoke(request)
    } catch (_: LensFailure) {
        showEditFormat(htmlView, getFormat, form).invoke(request)
    }
}
