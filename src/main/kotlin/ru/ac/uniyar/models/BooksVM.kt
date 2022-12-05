package ru.ac.uniyar.models

import org.http4k.core.Request
import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.web.lens.authorLoginLens
import ru.ac.uniyar.web.lens.bookNameLens
import ru.ac.uniyar.web.lens.genreNameLens

class BooksVM(
    pager: Pager,
    val books: List<Book>,
    val authors: List<Author>,
    val genres: List<Genre>,
    val request: Request
) : PagerVM(pager) {
    val selectedGenre = genreNameLens(request)
    val selectedAuthor = authorLoginLens(request)
    val bookName = bookNameLens(request)
}
