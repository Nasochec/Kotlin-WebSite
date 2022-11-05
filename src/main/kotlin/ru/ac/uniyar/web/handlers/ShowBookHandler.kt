package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.with
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.BookVM
import ru.ac.uniyar.web.lens.pageLens

fun showBook(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val indexPath = Path.of("index")
    val currentPage = pageLens(request)
    indexPath(request).toIntOrNull()?.let { id ->
        operationHolder.getBook.get(id)
    }?.let { book ->
        val author = operationHolder.getAuthor.get(book.authorId)
        val genre = operationHolder.getGenre.get(book.genreId)
        val chapters = operationHolder.getBookChapters.list(book.id, currentPage)
        val viewModel = BookVM(book, author!!, genre!!, chapters, currentPage, request.uri)
        Response(Status.OK).with(htmlView of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
