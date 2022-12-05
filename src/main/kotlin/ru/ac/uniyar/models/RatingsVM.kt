package ru.ac.uniyar.models

import ru.ac.uniyar.domain.entities.Rating

class RatingsVM(
    pager: Pager,
    val ratings: List<Rating>
) : PagerVM(pager) {
    val addButtonUri = "/rating/new"
}
