package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.BookFormat
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Books
import ru.ac.uniyar.domain.Rars
import ru.ac.uniyar.models.AddBookVM

val nameLens = FormField.nonEmptyString().required("name", "Название книги")
val authorLens = FormField.nonEmptyString().required("author", "Фвтор книги")
val rarsLens = FormField.nonEmptyString().required("rars", "Возрасной рейтинг книги")
val bookFormatLens = FormField.nonEmptyString().required("format", "Формат книги")
val genreLens = FormField.nonEmptyString().required("genre", "Жанр книги")
val annotationLens = FormField.string().required("annotation", "")//TODO
val summaryLens = FormField.string().required("summary", "")
val textLens = FormField.string().required("text", "")
val formLens = Body.webForm(
    Validator.Feedback,
    nameLens,
    authorLens,
    rarsLens,
    bookFormatLens,
    genreLens,
    annotationLens,
    summaryLens,
    textLens
).toLens()

fun addNewBook(htmlView: BiDiBodyLens<ViewModel>): HttpHandler = {
    val viewModel = AddBookVM()
    Response(Status.OK).with(htmlView of viewModel)
}

fun addBook(htmlView: BiDiBodyLens<ViewModel>, books: Books): HttpHandler = { request ->

    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            books.addBook(
                Book(
                    nameLens(form),
                    authorLens(form),
                    Rars.valueOf(rarsLens(form)),
                    BookFormat.valueOf(bookFormatLens(form)),
                    genreLens(form),
                    annotationLens(form),
                    summaryLens(form),
                    textLens(form)
                )
            )
            Response(Status.FOUND).header("location", "/books/" + (books.getBooksNumber() - 1))
        } else {
            //println(form.fields.get("len1").orEmpty().getOrElse(0, { "" }))
            Response(Status.BAD_REQUEST).with(htmlView of AddBookVM(form))
        }
    }
    catch( lf :LensFailure){
        Response(Status.BAD_REQUEST).with(htmlView of AddBookVM(form))
    }


}