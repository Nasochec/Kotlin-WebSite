package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.db.PageLenght
import ru.ac.uniyar.domain.db.tables.BookTable

class GetGenreBooks(
    private val database: Database
) {
    /**Возвращает 10 последних добавленных книг заданного жанра**/
    fun list(genreId: Int): List<Book> =
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
            .where { BookTable.genreId eq genreId }
            .orderBy(BookTable.creationDate.desc())
            .limit(PageLenght)
            .mapNotNull(Book::fromResultSet)
}
