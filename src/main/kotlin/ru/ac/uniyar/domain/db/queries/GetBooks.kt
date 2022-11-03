package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.db.PageLenght
import ru.ac.uniyar.domain.db.tables.BookTable

class GetBooks(
    private val database: Database
) {
    /**Возвращает список из не более чем 10 книг с применением фильтрации и постраничным выводом**/
    fun list(page: Int, name: String = "", authorId: Int? = null, genreId: Int? = null): List<Book> =
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
            .where {
                (BookTable.name.toLowerCase() like "%${name.lowercase()}%") and
                    ((authorId == null) or (BookTable.authorId eq (authorId ?: 0))) and
                    ((genreId == null) or (BookTable.genreId eq (genreId ?: 0)))
            }
            .orderBy(BookTable.creationDate.desc())
            .limit((page - 1) * PageLenght, PageLenght)
            .mapNotNull(Book::fromResultSet)

    /**Возвращает список всех книг**/
    fun listAll(): List<Book> =
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
}
