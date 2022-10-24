package ru.ac.uniyar.web.routers

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import ru.ac.uniyar.domain.Books
import ru.ac.uniyar.web.handlers.*
import ru.ac.uniyar.web.lens.HTMLView

fun app( books: Books): HttpHandler =
    routes(
        "/ping" bind Method.GET to ping(),

        "/" bind Method.GET to showMainPage(HTMLView),

        "/books" bind Method.GET to showAllBooks(HTMLView, books),

        "/books/new" bind Method.GET to addNewBook(HTMLView),

        "/books/new" bind Method.POST to addBook(HTMLView,books),

        "/books/{index}" bind Method.GET to showBook(HTMLView, books),

        static(ResourceLoader.Classpath("/ru/ac/uniyar/public"))
    )