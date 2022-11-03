package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.StatisticsVM


fun showStatistics(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val authorNumber = operationHolder.countAuthors.count()

    Response(Status.OK).with(htmlView of StatisticsVM(authorNumber))
}