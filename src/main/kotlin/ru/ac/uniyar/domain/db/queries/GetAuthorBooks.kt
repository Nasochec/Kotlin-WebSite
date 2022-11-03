package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.db.PageLenght
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
            .limit(PageLenght)
            .mapNotNull(Book::fromResultSet)
}
