package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import ru.ac.uniyar.domain.db.queries.GetStatistic
import ru.ac.uniyar.models.StatisticsVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showStatistics(
    htmlView: ContextAwareViewRenderer,
    statistic: GetStatistic
): HttpHandler = { request ->
    val authorsNumber = statistic.countAuthors()
    val booksNumber = statistic.countBooks()
    val chaptersNumber = statistic.countChapters()
    val authorWithMostBooks = statistic.getAuthorWithMostBooks()
    val authorWithMostChapters = statistic.getAuthorWithMostChapters()
    val bookWithMostChapters = statistic.getBookWithMostChapters()
    val genreWithMostBooks = statistic.getGenreWithMostBooks()
    Response(Status.OK).with(
        htmlView(request) of StatisticsVM(
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
