package ru.ac.uniyar.domain.db.queries.results

import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre

data class GenreFullData(
    val genre: Genre,
    val books: List<Book>
)
