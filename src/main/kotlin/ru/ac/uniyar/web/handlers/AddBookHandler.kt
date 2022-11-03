package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.BookFormat
import ru.ac.uniyar.domain.Rars
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.AddBookVM

fun addNewBook(
    htmlView: BiDiBodyLens<ViewModel>,
    operationHolder: OperationHolder,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    val authorIdQueryLens = Query.int().optional("authorId")
    val id = authorIdQueryLens(request)
    val authors = //if (id != null)
    //    listOf(operationHolder.getAuthor.get(id)!!)
   // else
        operationHolder.getAuthors.listAll()
    val genres = operationHolder.getGenres.listAll()
    Response(Status.OK).with(htmlView of AddBookVM(form, authors, genres, id))
}

fun addBook(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val nameLens = FormField.nonEmptyString().required("name", "Название книги")
    val authorIdLens = FormField.int().required("author", "Автор книги")
    val rarsLens = FormField.nonEmptyString().required("rars", "Возрасной рейтинг книги")
    val formatLens = FormField.nonEmptyString().required("format", "Формат книги")
    val genreIdLens = FormField.int().required("genre", "Жанр книги")
    val annotationLens = FormField.string().required("annotation", "") // TODO
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens,
        authorIdLens,
        rarsLens,
        formatLens,
        genreIdLens,
        annotationLens
    ).toLens()

    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            operationHolder.addBook.insert(
                nameLens(form),
                authorIdLens(form).toInt(),
                genreIdLens(form).toInt(),
                Rars.valueOf(rarsLens(form)),
                BookFormat.valueOf(formatLens(form)),
                annotationLens(form)
            )
            val book = operationHolder.getBook.getNewest()!!
            Response(Status.FOUND).header("location", "/book/${book.id}")
        } else {
            addNewBook(htmlView, operationHolder, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        addNewBook(htmlView, operationHolder, form).invoke(request)
    }
}
