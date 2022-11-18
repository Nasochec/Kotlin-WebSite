package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.BookTable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Book(
    val id: Int,
    val name: String,
    val authorId: Int,
    val rating: Rars,
    val format: BookFormat,
    val genreId: Int,
    val annotation: String,
    val creationDate: LocalDateTime
) {
    val creationDateFormatted: String =
        creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

    companion object {
        fun fromResultSet(row: QueryRowSet): Book? =
            try {
                Book(
                    row[BookTable.id]!!,
                    row[BookTable.name]!!,
                    row[BookTable.authorId]!!,
                    Rars.valueOf(row[BookTable.rating]!!),
                    BookFormat.valueOf(row[BookTable.format]!!),
                    row[BookTable.genreId]!!,
                    row[BookTable.annotation]!!,
                    row[BookTable.creationDate]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}