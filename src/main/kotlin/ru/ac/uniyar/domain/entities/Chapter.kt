package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.ChapterTable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Chapter(
    val bookId: Int,
    val number: Int,
    val name: String?,
    val text: String,
    val creationDate: LocalDateTime,
    val isVisible: Boolean,
    val numberOfReads: Int = 0,
    val isRead: Boolean? = null
) {
    val creationDateFormatted: String =
        creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

    companion object {
        fun fromResultSet(row: QueryRowSet): Chapter? =
            try {
                Chapter(
                    row[ChapterTable.bookId]!!,
                    row[ChapterTable.number]!!,
                    row[ChapterTable.name],
                    row[ChapterTable.text]!!,
                    row[ChapterTable.creationDate]!!,
                    row[ChapterTable.isVisible]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
