package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.tables.ChapterTable

class GetNextChapterNumber (
    private val database: Database
) {
    fun get(bookId:Int,number: Int): Int? =
        database
            .from(ChapterTable)
            .select(
                ChapterTable.bookId,
                ChapterTable.number
            )
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number greater number) }
            .orderBy(ChapterTable.number.asc())
            .limit(1)
            .mapNotNull{row->row[ChapterTable.number]}
            .firstOrNull()
}
