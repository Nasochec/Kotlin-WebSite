package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.db.PageLenght
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable

class GetAuthors(
    private val database: Database
) {
    /**Возвращает список состоящий из не более 10 асторов, с пименением сортировки**/
    fun list(page: Int, name: String = "", genreId: Int? = null): List<Author> =
        database
            .from(AuthorTable)
            .leftJoin(BookTable, AuthorTable.id eq BookTable.authorId)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name, BookTable.genreId)
            .where {
                (AuthorTable.name.toLowerCase() like "%${name.lowercase()}%") and
                        ((genreId == null) or (BookTable.genreId eq (genreId ?: 0)))
            }
            .orderBy(AuthorTable.creationDate.desc())
            .limit((page - 1) * PageLenght, PageLenght)
            .mapNotNull(Author::fromResultSet)
            .distinct()

    /**Возвращает список всех авторов**/
    fun listAll(): List<Author> =
        database
            .from(AuthorTable)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name)
            .mapNotNull(Author::fromResultSet)
}
