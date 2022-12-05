package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.db.tables.ReadHistoryTable
import ru.ac.uniyar.domain.entities.Chapter

class GetChapters(private val database: Database) {
    companion object {
        private val readCount = count(ReadHistoryTable.userLogin).aliased("readCount")

        private val name = min(ChapterTable.name).aliased("name")
        private val isVisible = min(ChapterTable.isVisible).aliased("isVisible")
        private val text = min(ChapterTable.text).aliased("text")
        private val creationDate = min(ChapterTable.creationDate).aliased("creationDate")
    }

    fun list(pageNumber: Int, bookId: Int, showNotVisible: Boolean = false, userLogin: String): List<Chapter> =
        database
            .from(ChapterTable)
            .leftJoin(
                ReadHistoryTable,
                (ReadHistoryTable.chapterBookId eq ChapterTable.bookId) and (ReadHistoryTable.chapterNumber eq ChapterTable.number)
            )
            .select(
                readCount, name, isVisible, text, creationDate, ChapterTable.number, ChapterTable.bookId,
                (max(ReadHistoryTable.userLogin eq (userLogin))).aliased("isRead")
            )
            .where(ChapterTable.bookId eq bookId and (showNotVisible or (ChapterTable.isVisible)))
            .groupBy(ChapterTable.number, ChapterTable.bookId)
            .orderBy(ChapterTable.number.asc())
            .limit((pageNumber - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull { row ->
                try {
                    Chapter(
                        row[ChapterTable.bookId]!!,
                        row[ChapterTable.number]!!,
                        row[name],
                        row[text]!!,
                        row[creationDate]!!,
                        row[isVisible]!!,
                        row[readCount]!!,
                        row[(max(ReadHistoryTable.userLogin eq (userLogin ?: ""))).aliased("isRead")]!!
                    )
                } catch (_: NullPointerException) {
                    null
                }
            }
}
