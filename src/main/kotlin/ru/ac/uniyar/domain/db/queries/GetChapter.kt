package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greater
import org.ktorm.dsl.less
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.entities.Chapter
import ru.ac.uniyar.domain.entities.ChapterFullData

class GetChapter(
    private val database: Database,
    private val getBook: GetBook,
    private val getAuthor: GetAuthor
) {
    fun get(bookId: Int, number: Int): Chapter? =
        database
            .from(ChapterTable)
            .select()
            .where { ChapterTable.bookId eq bookId and (ChapterTable.number eq number) }
            .mapNotNull(Chapter::fromResultSet)
            .firstOrNull()

    fun getFullData(bookId: Int, number: Int): ChapterFullData? {
        val chapter = get(bookId, number) ?: return null
        val book = getBook.getNotNull(bookId)
        return ChapterFullData(
            chapter,
            book,
            getAuthor.getNotNull(book.authorId),
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
