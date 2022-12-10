package ru.ac.uniyar.models

import ru.ac.uniyar.domain.db.queries.results.BookFullData
import java.time.LocalDateTime

class BookVM(
    pager: Pager,
    val bookFullData: BookFullData
) : PagerVM(pager) {
    val addButtonUri = "/chapter/new?bookId=${bookFullData.book.id}"
    val annotationParagraphs = bookFullData.book.annotation.split("\\n", "\n")
    val bookNeededDate = LocalDateTime.now().minusYears(bookFullData.book.rating.neededAge.toLong())
}
