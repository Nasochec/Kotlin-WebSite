package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.BookTable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Book(
    val id: Int,
    val name: String,
    val authorLogin: String,
    val rating: Rating,
    val format: Format,
    val genre: Genre,
    val annotation: String,
    val creationDate: LocalDateTime,
    val neededAgeSetByAdmin: Boolean
) {
    val creationDateFormatted: String =
        creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

    companion object {
        fun fromResultSet(row: QueryRowSet): Book? =
            try {
                Book(
                    row[BookTable.id]!!,
                    row[BookTable.name]!!,
                    row[BookTable.authorLogin]!!,
                    Rating(row[BookTable.neededAge]!!),
                    Format(row[BookTable.formatName]!!),
                    Genre(row[BookTable.genreName]!!),
                    row[BookTable.annotation]!!,
                    row[BookTable.creationDate]!!,
                    row[BookTable.neededAgeSetByAdmin]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
