package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.BookFormat
import ru.ac.uniyar.domain.Rars
import ru.ac.uniyar.domain.db.tables.BookTable
import java.time.LocalDateTime

class AddBook(
    private val database: Database
) {
    fun insert(
        name: String,
        authorId: Int,
        genreId: Int,
        rating: Rars,
        format: BookFormat,
        annotation: String
    ) =
        database
            .insert(BookTable) {
                set(BookTable.name, name)
                set(BookTable.authorId, authorId)
                set(BookTable.genreId, genreId)
                set(BookTable.rating, rating.name)
                set(BookTable.format, format.name)
                set(BookTable.annotation, annotation)
                set(BookTable.creationDate, LocalDateTime.now())
            }
}
