package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.models.MainPageVM

fun showMainPage(htmlView : BiDiBodyLens<ViewModel>): HttpHandler = {
    val viewModel = MainPageVM()
//    val renderer = PebbleTemplates().HotReload("src/main/resources")
//    val view = Body.viewModel(renderer, ContentType.TEXT_HTML).toLens()
    Response(Status.OK).with(htmlView of viewModel)
}