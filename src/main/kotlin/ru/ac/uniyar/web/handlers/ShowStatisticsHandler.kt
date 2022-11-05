package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.StatisticsVM

fun showStatistics(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = {
    val authorsNumber = operationHolder.countAuthors.count()
    val booksNumber = operationHolder.countBooks.count()
    val chaptersNumber = operationHolder.countChapters.count()
    val authorWithMostBooks = operationHolder.statistic.getAuthorWithMostBooks()
    val authorWithMostChapters = operationHolder.statistic.getAuthorWithMostChapters()
    val bookWithMostChapters = operationHolder.statistic.getBookWithMostChapters()
    val genreWithMostBooks = operationHolder.statistic.getGenreWithMostBooks()
    Response(Status.OK).with(
        htmlView of StatisticsVM(
            authorsNumber,
            booksNumber,
            chaptersNumber,
            authorWithMostBooks,
            authorWithMostChapters,
            bookWithMostChapters,
            genreWithMostBooks
        )
    )
}
