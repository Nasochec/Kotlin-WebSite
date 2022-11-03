package ru.ac.uniyar.web.routers

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.then
import org.http4k.lens.BiDiBodyLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.web.filters.errorRequestFilter
import ru.ac.uniyar.web.handlers.*

fun app(operationHolder: OperationHolder, htmlView: BiDiBodyLens<ViewModel>): HttpHandler =
    errorRequestFilter(htmlView).then(
        routes(
            "/ping" bind Method.GET to ping(),

            "/" bind Method.GET to showMainPage(htmlView),

            "/statistics" bind Method.GET to showStatistics(htmlView,operationHolder),

            "/author/new" bind Method.GET to addNewAuthor(htmlView),

            "/author/new" bind Method.POST to addAuthor(htmlView, operationHolder),

            "/author/{index}" bind Method.GET to showAuthor(htmlView, operationHolder),

            "/authors" bind Method.GET to showAuthors(htmlView, operationHolder),

            "/books" bind Method.GET to showBooks(htmlView, operationHolder),

            "/book/new" bind Method.GET to addNewBook(htmlView, operationHolder),

            "/book/new" bind Method.POST to addBook(htmlView, operationHolder),

            "/book/{index}" bind Method.GET to showBook(htmlView, operationHolder),

            "/genre/{index}" bind Method.GET to showGenre(htmlView, operationHolder),

            "/chapter/new" bind Method.GET to addNewChapter(htmlView, operationHolder),

            "/chapter/new" bind Method.POST to addChapter(htmlView, operationHolder),

            "/chapter/{bookId}/{id}" bind Method.GET to showChapter(htmlView, operationHolder),

            static(ResourceLoader.Classpath("/ru/ac/uniyar/public"))
        )
    )
