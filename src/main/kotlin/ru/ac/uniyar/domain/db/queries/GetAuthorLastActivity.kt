package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable
import java.time.LocalDateTime

class GetAuthorLastActivity(
    private val database: Database
) {
    private val maxCreationDateAlias = max(ChapterTable.creationDate).aliased("maxCreationDateAlias")
    fun get(authorId: Int): LocalDateTime? = database
        .from(ChapterTable)
        .select(ChapterTable.bookId, maxCreationDateAlias)
        .groupBy(ChapterTable.bookId)
        .where(
            ChapterTable.bookId inList
                database
                    .from(BookTable)
                    .select(BookTable.id, BookTable.authorId)
                    .where { BookTable.authorId eq authorId }
                    .mapNotNull { row ->
                        row[BookTable.id]
                    }
        )
        .orderBy(maxCreationDateAlias.desc())
        .mapNotNull { row -> row[maxCreationDateAlias] }.firstOrNull()
}
