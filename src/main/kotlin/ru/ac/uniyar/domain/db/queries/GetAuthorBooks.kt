package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.BookTable

class GetAuthorBooks(
    private val database: Database
) {
    /**Возвращает 10 последних написанных автором книг**/
    fun list(authorId: Int): List<Book> =
        database
            .from(BookTable)
            .select(
                BookTable.id,
                BookTable.name,
                BookTable.authorId,
                BookTable.creationDate,
                BookTable.genreId,
                BookTable.annotation,
                BookTable.format,
                BookTable.rating
            )
            .where { BookTable.authorId eq authorId }
            .orderBy(BookTable.creationDate.desc())
            .limit(PAGE_LENGTH)
            .mapNotNull(Book::fromResultSet)
}
