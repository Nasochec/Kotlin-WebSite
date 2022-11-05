package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.groupBy
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.max
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable
import java.time.LocalDateTime

class GetAuthorLastActivity(
    private val database: Database
) {
    private val maxCreationDateAlias = max(ChapterTable.creationDate).aliased("maxCreationDateAlias")
    fun get(authorId: Int): LocalDateTime? = database
        .from(ChapterTable)
        .leftJoin(BookTable, ChapterTable.bookId eq BookTable.id)
        .select(BookTable.authorId, maxCreationDateAlias)
        .groupBy(BookTable.authorId)
        .where(BookTable.authorId eq authorId)
        .mapNotNull { row -> row[maxCreationDateAlias] }.firstOrNull()
}
