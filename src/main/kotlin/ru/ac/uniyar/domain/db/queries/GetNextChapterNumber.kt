package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greater
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.tables.ChapterTable

class GetNextChapterNumber(
    private val database: Database
) {
    fun get(bookId: Int, number: Int): Int? =
        database
            .from(ChapterTable)
            .select(
                ChapterTable.bookId,
                ChapterTable.number
            )
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number greater number) }
            .orderBy(ChapterTable.number.asc())
            .limit(1)
            .mapNotNull { row -> row[ChapterTable.number] }
            .firstOrNull()
}
