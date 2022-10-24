package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Books
import ru.ac.uniyar.models.BooksVM

fun showAllBooks(htmlView : BiDiBodyLens<ViewModel>, books: Books): HttpHandler = {
    val viewModel = BooksVM(books.getAllIndexed())
    Response(Status.OK).with(htmlView of viewModel)
}