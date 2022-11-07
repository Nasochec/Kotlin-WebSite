package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.db.tables.AUTHOR_NAME_MAX_LENGTH
import ru.ac.uniyar.models.AddAuthorVM

fun addNewAuthor(
    htmlView: BiDiBodyLens<ViewModel>,
    form: WebForm = WebForm(),
    errors:List<String> = listOf()
): HttpHandler = {
    Response(Status.OK).with(htmlView of AddAuthorVM(form,errors))
}

fun addAuthor(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Имя автора")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens
    ).toLens()
    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            val errors = mutableListOf<String>()
            if(nameLens(form).length > AUTHOR_NAME_MAX_LENGTH)
                errors.add("Длина имени автора не должна превышать $AUTHOR_NAME_MAX_LENGTH символов.")
            if(errors.isNotEmpty())
                return@handler addNewAuthor(htmlView, form,errors).invoke(request)
            operationHolder.addAuthor.insert(nameLens(form))
            val author = operationHolder.getAuthor.getNewest()!!
            Response(Status.FOUND).header("location", "/author/${author.id}")
        } else {
            addNewAuthor(htmlView, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        addNewAuthor(htmlView, form).invoke(request)
    }
}
