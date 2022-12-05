package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import ru.ac.uniyar.domain.db.queries.CountRatings
import ru.ac.uniyar.domain.db.queries.GetRatings
import ru.ac.uniyar.models.Pager
import ru.ac.uniyar.models.RatingsVM
import ru.ac.uniyar.web.lens.pageLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showRatings(
    htmlView: ContextAwareViewRenderer,
    getRatings: GetRatings,
    countRatings: CountRatings
): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val ratings = getRatings.list(currentPage)
    val pager = Pager(currentPage, request.uri, countRatings.count())
    Response(Status.OK).with(htmlView(request) of RatingsVM(pager, ratings))
}
