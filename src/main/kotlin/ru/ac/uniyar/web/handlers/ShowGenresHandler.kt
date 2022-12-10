package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.db.queries.CountGenres
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.models.GenresVM
import ru.ac.uniyar.models.Pager
import ru.ac.uniyar.web.lens.genreNameLens
import ru.ac.uniyar.web.lens.pageLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showGenres(
    htmlView: ContextAwareViewRenderer,
    getGenres: GetGenres,
    countGenres: CountGenres
): HttpHandler = { request ->
    val page = pageLens(request)
    val name = genreNameLens(request)
    val genres = getGenres.list(page, name)
    val genresCount = countGenres.countFiltered(name)
    val pager = Pager(page, request.uri, genresCount)
    Response(Status.OK).with(htmlView(request) of GenresVM(pager, genres, request))
}
