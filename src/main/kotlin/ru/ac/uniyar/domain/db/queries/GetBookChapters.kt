package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.dsl.asc
import ru.ac.uniyar.domain.Chapter
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.ChapterTable

class GetBookChapters(
    private val database: Database
) {
    fun list(bookId: Int, pageNumber: Int): List<Chapter> =
        database
            .from(ChapterTable)
            .select()
            .where { ChapterTable.bookId eq bookId }
            .orderBy(ChapterTable.number.asc())
            .limit((pageNumber - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Chapter::fromResultSet)
}
