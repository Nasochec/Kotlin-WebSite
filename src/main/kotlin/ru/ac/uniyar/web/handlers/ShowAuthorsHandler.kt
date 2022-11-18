package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.models.AuthorsVM
import ru.ac.uniyar.web.lens.authorNameLens
import ru.ac.uniyar.web.lens.genreIdLens
import ru.ac.uniyar.web.lens.pageLens

fun showAuthors(
    htmlView: BiDiBodyLens<ViewModel>,
    getAuthors: GetAuthors,
    getGenres: GetGenres
): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val name = authorNameLens(request)
    val genreId = genreIdLens(request)
    val authors = getAuthors.list(currentPage, name, genreId)
    val genres = getGenres.listAll()
    val view = AuthorsVM(authors, genres, request, currentPage)
    Response(Status.OK).with(htmlView of view)
}
