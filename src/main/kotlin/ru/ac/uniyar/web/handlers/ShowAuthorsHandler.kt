package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.AuthorsVM
import ru.ac.uniyar.web.lens.authorNameLens
import ru.ac.uniyar.web.lens.genreIdLens
import ru.ac.uniyar.web.lens.pageLens

fun showAuthors(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = { request ->
    val currentPage = pageLens(request)
    val name = authorNameLens(request)
    val genreId = genreIdLens(request)
    val authors = operationHolder.getAuthors.list(currentPage, name, genreId)
    val genres = operationHolder.getGenres.listAll()
    val view = AuthorsVM(authors, genres, request, currentPage)
    Response(Status.OK).with(htmlView of view)
}
