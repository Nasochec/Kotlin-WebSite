package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.count
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.groupBy
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.max
import org.ktorm.dsl.min
import org.ktorm.dsl.or
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.BookTable
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

    /**Возвращает список глав книги с учётом того, что не автору этих глав, скрытые главы отображать не надо, а также считает количество прочтений этих глав, а также находит прочтена ли каждая глава заданным пользователем.**/
    fun list(pageNumber: Int, bookId: Int, userLogin: String?): List<Chapter> =
        database
            .from(ChapterTable)
            .leftJoin(
                ReadHistoryTable,
                (ReadHistoryTable.chapterBookId eq ChapterTable.bookId) and (ReadHistoryTable.chapterNumber eq ChapterTable.number)
            )
            .leftJoin(BookTable, BookTable.id eq ChapterTable.bookId)
            .select(
                readCount, name, isVisible, text, creationDate, ChapterTable.number, ChapterTable.bookId,
                (
                    max(
                        ReadHistoryTable.userLogin eq (userLogin ?: "")
                    )
                    ).aliased("isRead") // в данном случаем поскольку переменная - булевская функция max эквивалентна созданию подзапроса и применения к нему EXISTS
            )
            .where(
                ChapterTable.bookId eq bookId and
                    ((BookTable.authorLogin eq (userLogin ?: "")) or (ChapterTable.isVisible))
            )
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
                        row[(max(ReadHistoryTable.userLogin eq (userLogin ?: ""))).aliased("isRead")]
                    )
                } catch (_: NullPointerException) {
                    null
                }
            }
}
