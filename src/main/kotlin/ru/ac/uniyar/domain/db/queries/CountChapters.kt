package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
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
    fun count(bookId: Int, showNotVisible: Boolean = false): Int =
        database
            .from(ChapterTable)
            .leftJoin(BookTable, ChapterTable.bookId eq BookTable.id)
            .select(chapterCount)
            .where((showNotVisible or (ChapterTable.isVisible)))
            .mapNotNull { row -> row[chapterCount] }
            .firstOrNull() ?: 0
}
