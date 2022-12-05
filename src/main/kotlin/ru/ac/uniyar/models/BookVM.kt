package ru.ac.uniyar.models

import ru.ac.uniyar.domain.db.queries.results.BookFullData

class BookVM(
    pager: Pager,
    val bookFullData: BookFullData,
    val isOwner: Boolean
) : PagerVM(pager) {
    val addButtonUri = "/chapter/new?bookId=${bookFullData.book.id}"
    val annotationParagraphs = bookFullData.book.annotation.split("\\n", "\n")
}
