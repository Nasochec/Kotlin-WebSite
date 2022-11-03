package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.db.tables.BookTable

class GetBook(
    private val database: Database
) {
    /**Возвращает книгу с заданным id или null если не найдёт**/
    fun get(id: Int): Book? =
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
            .where { BookTable.id eq id }
            .limit(1)
            .mapNotNull(Book::fromResultSet)
            .firstOrNull()
    /**Возвращает самую новую (самую позже добавленную) книгу**/
    fun getNewest(): Book? =
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
            .mapNotNull(Book::fromResultSet)
            .lastOrNull()
}