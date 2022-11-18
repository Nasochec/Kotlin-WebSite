package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.models.GenreVM

fun showGenre(htmlView: BiDiBodyLens<ViewModel>, getGenre: GetGenre): HttpHandler = { request ->
    val indexPath = Path.of("index")
    indexPath(request).toIntOrNull()?.let { id ->
        getGenre.getFullData(id)
    }?.let { genre ->
        Response(Status.OK).with(htmlView of GenreVM(genre))
    } ?: Response(Status.BAD_REQUEST)
}
