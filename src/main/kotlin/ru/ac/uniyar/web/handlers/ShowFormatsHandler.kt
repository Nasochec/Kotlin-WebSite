package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.db.queries.CountFormats
import ru.ac.uniyar.domain.db.queries.GetFormats
import ru.ac.uniyar.models.FormatsVM
import ru.ac.uniyar.models.Pager
import ru.ac.uniyar.web.lens.pageLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showFormats(
    htmlView: ContextAwareViewRenderer,
    getFormats: GetFormats,
    countFormats: CountFormats
): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val formats = getFormats.list(currentPage)
    val pager = Pager(currentPage, request.uri, countFormats.countFiltered())
    Response(Status.OK).with(htmlView(request) of FormatsVM(pager, formats))
}
