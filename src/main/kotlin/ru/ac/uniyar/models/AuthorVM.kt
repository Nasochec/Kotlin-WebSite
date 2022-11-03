package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Genre
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AuthorVM(
    val author: Author,
    val books: List<Book>,
    val genres: List<Genre>,
    val lastActivity: LocalDateTime?
) : ViewModel {
    val lastActivityDate = lastActivity?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    val moreBooksUri = "/books?authorId=${author.id}"
    val addBookUri = "/book/new?authorId=${author.id}"
}
