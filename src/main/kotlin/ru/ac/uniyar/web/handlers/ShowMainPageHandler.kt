package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.models.MainPageVM

fun showMainPage(htmlView: BiDiBodyLens<ViewModel>): HttpHandler = {
    val viewModel = MainPageVM()
    Response(Status.OK).with(htmlView of viewModel)
}
