package ru.ac.uniyar.domain

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.AuthorTable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Author(
    val id: Int,
    val name: String,
    val creationDate: LocalDateTime
) {

    val creationDateFormatted: String =
        creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

    companion object {
        fun fromResultSet(row: QueryRowSet): Author? =
            try {
                Author(
                    row[AuthorTable.id]!!,
                    row[AuthorTable.name]!!,
                    row[AuthorTable.creationDate]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
