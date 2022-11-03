package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.tables.ChapterTable

class GetPrevChapterNumber (
    private val database: Database
) {
    fun get(bookId:Int,number: Int): Int? =
        database
            .from(ChapterTable)
            .select(
                ChapterTable.bookId,
                ChapterTable.number
            )
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number less number) }
            .orderBy(ChapterTable.number.desc())
            .limit(1)
            .mapNotNull{row->row[ChapterTable.number]}
            .firstOrNull()
}
