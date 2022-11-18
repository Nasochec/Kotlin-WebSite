package ru.ac.uniyar.models

import org.http4k.core.Request
import org.http4k.core.query
import org.http4k.core.removeQuery
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.web.lens.authorIdLens
import ru.ac.uniyar.web.lens.bookNameLens
import ru.ac.uniyar.web.lens.genreIdLens

data class BooksVM(
    val books: List<Book>,
    val authors: List<Author>,
    val genres: List<Genre>,
    val request: Request,
    val currentPage: Int
) : ViewModel {
    val nextPageUri = request.uri.removeQuery("page").query("page", (currentPage + 1).toString()).toString()
    val prevPageUri = request.uri.removeQuery("page").query("page", (currentPage - 1).toString()).toString()
    val selectedGenre = genreIdLens(request)
    val selectedAuthor = authorIdLens(request)
    val bookName = bookNameLens(request)
}
