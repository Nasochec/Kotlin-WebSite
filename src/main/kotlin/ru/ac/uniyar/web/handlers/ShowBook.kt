package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Books
import ru.ac.uniyar.models.BookVM

val indexPath = Path.of("index")

fun showBook(htmlView: BiDiBodyLens<ViewModel>, book: Book): HttpHandler = {
    val viewModel = BookVM(book)
    Response(Status.OK).with(htmlView of viewModel)
}
fun showBook(htmlView: BiDiBodyLens<ViewModel>, books: Books): HttpHandler = {
    //TODO Добавьте обработку исключения LensFailure.
    // В случае возникновения исключительной ситуации обработчик должен возвращать ответ с кодом 400, BAD_REQUEST.
    indexPath(it).toIntOrNull()?.let {
        if(it<0||it>=books.getBooksNumber())
            null
        else
            books.getBook(it)
    }?.let {
        val viewModel = BookVM(it)
        Response(Status.OK).with(htmlView of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}