package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Chapter
import ru.ac.uniyar.domain.db.tables.ChapterTable

class GetChapter(
    private val database: Database
) {
    fun get(bookId: Int, number: Int): Chapter? =
        database
            .from(ChapterTable)
            .select(
                ChapterTable.bookId,
                ChapterTable.number,
                ChapterTable.creationDate,
                ChapterTable.name,
                ChapterTable.text
            )
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number eq number) }
            .mapNotNull(Chapter::fromResultSet)
            .firstOrNull()
}
