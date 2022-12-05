package ru.ac.uniyar.models

import ru.ac.uniyar.domain.entities.Genre

class GenresVM(
    pager: Pager,
    val genres: List<Genre>
) : PagerVM(pager) {
    val addButtonUri = "/genre/new"
}
