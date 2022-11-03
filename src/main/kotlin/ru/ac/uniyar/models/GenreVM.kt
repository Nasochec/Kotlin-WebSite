package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Genre
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GenreVM(
    val genre: Genre,
    val books: List<Book>
) : ViewModel {
    val moreBooksUri = "/books?genreId=${genre.id}"
}
