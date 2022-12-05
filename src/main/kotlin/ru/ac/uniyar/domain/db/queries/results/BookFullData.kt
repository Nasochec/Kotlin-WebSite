package ru.ac.uniyar.domain.db.queries.results

import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Chapter

data class BookFullData(
    val book: Book,
    val author: Author,
    val chapters: List<Chapter>,
    val chaptersNumber: Int
)
