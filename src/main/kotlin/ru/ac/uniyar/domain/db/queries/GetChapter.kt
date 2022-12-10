package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.count
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greater
import org.ktorm.dsl.groupBy
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.less
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.min
import org.ktorm.dsl.or
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.queries.results.ChapterFullData
import ru.ac.uniyar.domain.db.tables.BookTable
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

    /**Возвращает главу с учётом того, что не автору этой главы если она скрытая, её отображать не надо**/
    fun get(bookId: Int, number: Int, userLogin: String): Chapter? =
        database
            .from(ChapterTable)
            .leftJoin(BookTable, BookTable.id eq ChapterTable.bookId)
            .leftJoin(
                ReadHistoryTable,
                (ReadHistoryTable.chapterBookId eq ChapterTable.bookId) and (ReadHistoryTable.chapterNumber eq ChapterTable.number)
            )
            .select(readCount, name, isVisible, text, creationDate, ChapterTable.number, ChapterTable.bookId)
            .where {
                ChapterTable.bookId eq bookId and (ChapterTable.number eq number) and
                    (ChapterTable.isVisible or (BookTable.authorLogin eq (userLogin)))
            }
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

    /**Возвращает полную информацию о главе с учётом того, что не автору этой главы если она скрытая, её отображать не надо**/
    fun getFullData(bookId: Int, number: Int, userLogin: String): ChapterFullData? {
        val chapter = get(bookId, number, userLogin) ?: return null
        val book = getBook.getNotNull(bookId)
        return ChapterFullData(
            chapter,
            book,
            getAuthor.getNotNull(book.authorLogin),
            getNextChapterNumber(bookId, number, userLogin),
            getPrevChapterNumber(bookId, number, userLogin)
        )
    }

    /**Возвращает номер следующей главы с учётом того, что не автору этой главы если она скрытая, её отображать не надо**/
    private fun getNextChapterNumber(bookId: Int, number: Int, userLogin: String?): Int? =
        database
            .from(ChapterTable)
            .leftJoin(BookTable, BookTable.id eq ChapterTable.bookId)
            .select(
                ChapterTable.bookId,
                ChapterTable.number
            )
            .where {
                (ChapterTable.bookId eq bookId) and (ChapterTable.number greater number) and
                    (ChapterTable.isVisible or (BookTable.authorLogin eq (userLogin ?: "")))
            }
            .orderBy(ChapterTable.number.asc())
            .limit(1)
            .mapNotNull { row -> row[ChapterTable.number] }
            .firstOrNull()

    /**Возвращает номер предыдущей главы с учётом того, что не автору этой главы если она скрытая, её отображать не надо**/
    private fun getPrevChapterNumber(bookId: Int, number: Int, userLogin: String?): Int? =
        database
            .from(ChapterTable)
            .leftJoin(BookTable, BookTable.id eq ChapterTable.bookId)
            .select(
                ChapterTable.bookId,
                ChapterTable.number
            )
            .where {
                ChapterTable.bookId eq bookId and (ChapterTable.number less number) and
                    (ChapterTable.isVisible or (BookTable.authorLogin eq (userLogin ?: "")))
            }
            .orderBy(ChapterTable.number.desc())
            .limit(1)
            .mapNotNull { row -> row[ChapterTable.number] }
            .firstOrNull()
}
