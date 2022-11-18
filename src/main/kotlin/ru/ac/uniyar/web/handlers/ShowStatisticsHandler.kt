package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.GetStatistic
import ru.ac.uniyar.models.StatisticsVM

fun showStatistics(htmlView: BiDiBodyLens<ViewModel>, statistic: GetStatistic): HttpHandler = {
    val authorsNumber = statistic.countAuthors()
    val booksNumber = statistic.countBooks()
    val chaptersNumber = statistic.countChapters()
    val authorWithMostBooks = statistic.getAuthorWithMostBooks()
    val authorWithMostChapters = statistic.getAuthorWithMostChapters()
    val bookWithMostChapters = statistic.getBookWithMostChapters()
    val genreWithMostBooks = statistic.getGenreWithMostBooks()
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
