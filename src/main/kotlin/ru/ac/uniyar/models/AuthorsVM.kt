package ru.ac.uniyar.models

import org.http4k.core.Request
import org.http4k.core.query
import org.http4k.core.removeQuery
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.Genre
import ru.ac.uniyar.web.lens.authorNameLens
import ru.ac.uniyar.web.lens.genreIdLens

data class AuthorsVM(
    val authors: List<Author>,
    val genres:List<Genre>,
    val request: Request,
    val currentPage:Int
) : ViewModel {
    val selectedGenre = genreIdLens(request)
    val authorName = authorNameLens(request)
    val nextPageUri = request.uri.removeQuery("page").query("page", (currentPage + 1).toString()).toString()
    val prevPageUri = request.uri.removeQuery("page").query("page", (currentPage - 1).toString()).toString()
}
