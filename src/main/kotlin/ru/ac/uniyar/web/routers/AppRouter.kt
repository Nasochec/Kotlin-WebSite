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
import ru.ac.uniyar.web.handlers.addAuthor
import ru.ac.uniyar.web.handlers.addBook
import ru.ac.uniyar.web.handlers.addChapter
import ru.ac.uniyar.web.handlers.addNewAuthor
import ru.ac.uniyar.web.handlers.addNewBook
import ru.ac.uniyar.web.handlers.addNewChapter
import ru.ac.uniyar.web.handlers.ping
import ru.ac.uniyar.web.handlers.showAuthor
import ru.ac.uniyar.web.handlers.showAuthors
import ru.ac.uniyar.web.handlers.showBook
import ru.ac.uniyar.web.handlers.showBooks
import ru.ac.uniyar.web.handlers.showChapter
import ru.ac.uniyar.web.handlers.showGenre
import ru.ac.uniyar.web.handlers.showMainPage
import ru.ac.uniyar.web.handlers.showStatistics

fun app(operationHolder: OperationHolder, htmlView: BiDiBodyLens<ViewModel>): HttpHandler =
    errorRequestFilter(htmlView).then(
        routes(
            "/" bind Method.GET to showMainPage(htmlView),

            "/ping" bind Method.GET to ping(),

            "/statistics" bind Method.GET to showStatistics(htmlView, operationHolder.statistic),

            "/authors" bind Method.GET to showAuthors(htmlView, operationHolder.getAuthors, operationHolder.getGenres),
            "/author/new" bind Method.GET to addNewAuthor(htmlView),
            "/author/new" bind Method.POST to addAuthor(htmlView, operationHolder),

            "/books" bind Method.GET to showBooks(htmlView, operationHolder.getBooks, operationHolder.getAuthors, operationHolder.getGenres),
            "/book/new" bind Method.GET to addNewBook(htmlView, operationHolder),
            "/book/new" bind Method.POST to addBook(htmlView, operationHolder),

            "/chapter/new" bind Method.GET to addNewChapter(htmlView, operationHolder),
            "/chapter/new" bind Method.POST to addChapter(htmlView, operationHolder),

            "/author/{index}" bind Method.GET to showAuthor(htmlView, operationHolder.getAuthor),
            "/book/{index}" bind Method.GET to showBook(htmlView, operationHolder.getBook),
            "/chapter/{bookId}/{id}" bind Method.GET to showChapter(htmlView, operationHolder.getChapter),
            "/genre/{index}" bind Method.GET to showGenre(htmlView, operationHolder.getGenre),

            static(ResourceLoader.Classpath("/ru/ac/uniyar/public"))
        )
    )
