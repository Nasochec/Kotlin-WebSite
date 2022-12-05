package ru.ac.uniyar.models

import ru.ac.uniyar.domain.entities.Format

class FormatsVM(
    pager: Pager,
    val formats: List<Format>
) : PagerVM(pager) {
    val addButtonUri = "/format/new"
}
