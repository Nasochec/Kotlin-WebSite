package ru.ac.uniyar.models

import org.http4k.core.Request
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.web.lens.genreNameLens

class GenresVM(
    pager: Pager,
    val genres: List<Genre>,
    request: Request
) : PagerVM(pager) {
    val addButtonUri = "/genre/new"
    val genreName = genreNameLens(request)
}
