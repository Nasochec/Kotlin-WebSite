package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.AddAuthorVM

fun addNewAuthor(
    htmlView: BiDiBodyLens<ViewModel>,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    Response(Status.OK).with(htmlView of AddAuthorVM(form))
}

fun addAuthor(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val nameLens = FormField.nonEmptyString().required("name", "Имя автора")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens
    ).toLens()
    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
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
