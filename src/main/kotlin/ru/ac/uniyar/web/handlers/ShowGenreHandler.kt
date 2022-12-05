package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.models.GenreVM
import ru.ac.uniyar.web.lens.path.genreNamePathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showGenre(
    htmlView: ContextAwareViewRenderer,
    getGenre: GetGenre
): HttpHandler = { request ->
    genreNamePathLens(request)?.let { name ->
        getGenre.getFullData(name)
    }?.let { genre ->
        Response(Status.OK).with(htmlView(request) of GenreVM(genre))
    } ?: Response(Status.BAD_REQUEST)
}
