package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.queries.results.ChapterFullData
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.db.tables.ReadHistoryTable
import ru.ac.uniyar.domain.entities.Chapter

class GetChapter(
    private val database: Database,
    private val getBook: GetBook,
    private val getAuthor: GetAuthor
) {
    companion object {
        private val readCount = count(ReadHistoryTable.userLogin).aliased("readCount")
        private val name = min(ChapterTable.name).aliased("name")
        private val isVisible = min(ChapterTable.isVisible).aliased("isVisible")
        private val text = min(ChapterTable.text).aliased("text")
        private val creationDate = min(ChapterTable.creationDate).aliased("creationDate")
    }
    fun get(bookId: Int, number: Int): Chapter? =
        database
            .from(ChapterTable)
            .leftJoin(
                ReadHistoryTable,
                (ReadHistoryTable.chapterBookId eq ChapterTable.bookId) and (ReadHistoryTable.chapterNumber eq ChapterTable.number)
            )
            .select(readCount, name, isVisible, text, creationDate, ChapterTable.number, ChapterTable.bookId)
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number eq number) }
            .groupBy(ChapterTable.number, ChapterTable.bookId)
            .mapNotNull { row ->
                try {
                    Chapter(
                        row[ChapterTable.bookId]!!,
                        row[ChapterTable.number]!!,
                        row[name],
                        row[text]!!,
                        row[creationDate]!!,
                        row[isVisible]!!,
                        row[readCount]!!
                    )
                } catch (_: NullPointerException) {
                    null
                }
            }
            .firstOrNull()

    fun getFullData(bookId: Int, number: Int): ChapterFullData? {
        val chapter = get(bookId, number) ?: return null
        val book = getBook.getNotNull(bookId)
        return ChapterFullData(
            chapter,
            book,
            getAuthor.getNotNull(book.authorLogin),
            getNextChapterNumber(bookId, number),
            getPrevChapterNumber(bookId, number)
        )
    }

    private fun getNextChapterNumber(bookId: Int, number: Int): Int? =
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

    private fun getPrevChapterNumber(bookId: Int, number: Int): Int? =
        database
            .from(ChapterTable)
            .select(
                ChapterTable.bookId,
                ChapterTable.number
            )
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number less number) }
            .orderBy(ChapterTable.number.desc())
            .limit(1)
            .mapNotNull { row -> row[ChapterTable.number] }
            .firstOrNull()
}
