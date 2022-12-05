package ru.ac.uniyar.domain.db.queries.results

import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AuthorFullData(
    val author: Author,
    val books: List<Book>,
    val genres: List<Genre>,
    val lastActivity: LocalDateTime?
) {
    val lastActivityFormatted: String? =
        lastActivity?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
}
