package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Chapter
import ru.ac.uniyar.domain.db.PageLenght
import ru.ac.uniyar.domain.db.tables.ChapterTable

class GetBookChapters(
    private val database: Database
) {
    fun list(bookId: Int, pageNumber: Int): List<Chapter> =
        database
            .from(ChapterTable)
            .select(
                ChapterTable.bookId,
                ChapterTable.number,
                ChapterTable.creationDate,
                ChapterTable.name,
                ChapterTable.text
            )
            .where { ChapterTable.bookId eq bookId }
            .orderBy(ChapterTable.number.asc())
            .limit((pageNumber - 1) * PageLenght, PageLenght)
            .mapNotNull(Chapter::fromResultSet)
}
