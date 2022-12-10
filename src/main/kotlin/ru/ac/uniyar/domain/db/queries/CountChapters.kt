package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable

class CountChapters(
    private val database: Database
) {
    companion object {
        private val chapterCount = org.ktorm.dsl.count(ChapterTable.number).aliased("chapterCount")
    }
    fun count(): Int =
        database
            .from(ChapterTable)
            .select(chapterCount)
            .mapNotNull { row -> row[chapterCount] }
            .firstOrNull() ?: 0
    fun count(bookId: Int, userLogin: String?): Int =
        database
            .from(ChapterTable)
            .leftJoin(BookTable, BookTable.id eq ChapterTable.bookId)
            .select(chapterCount)
            .where(ChapterTable.bookId eq bookId and ((BookTable.authorLogin eq (userLogin ?: "")) or (ChapterTable.isVisible)))
            .mapNotNull { row -> row[chapterCount] }
            .firstOrNull() ?: 0
}
