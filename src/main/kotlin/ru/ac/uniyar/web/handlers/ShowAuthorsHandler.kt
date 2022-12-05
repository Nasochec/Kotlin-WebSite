package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import ru.ac.uniyar.domain.db.queries.CountAuthors
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.models.AuthorsVM
import ru.ac.uniyar.models.Pager
import ru.ac.uniyar.web.lens.authorNameLens
import ru.ac.uniyar.web.lens.genreNameLens
import ru.ac.uniyar.web.lens.pageLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAuthors(
    htmlView: ContextAwareViewRenderer,
    getAuthors: GetAuthors,
    countAuthors: CountAuthors,
    getGenres: GetGenres
): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val name = authorNameLens(request)
    val genre = genreNameLens(request)
    val authors = getAuthors.list(currentPage, name, genre)
    val authorsCount = countAuthors.countFiltered(name, genre)
    val genres = getGenres.listAll()
    val pager = Pager(currentPage, request.uri, authorsCount)
    val view = AuthorsVM(pager, authors, genres, request)
    Response(Status.OK).with(htmlView(request) of view)
}
