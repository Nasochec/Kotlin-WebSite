package ru.ac.uniyar.models

import org.http4k.core.Request
import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.web.lens.authorNameLens
import ru.ac.uniyar.web.lens.genreNameLens

class AuthorsVM(
    pager: Pager,
    val authors: List<Author>,
    val genres: List<Genre>,
    request: Request
) : PagerVM(pager) {
    val selectedGenre = genreNameLens(request)
    val authorName = authorNameLens(request)
}
