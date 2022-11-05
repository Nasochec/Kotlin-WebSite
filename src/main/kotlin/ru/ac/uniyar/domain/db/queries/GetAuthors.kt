package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.or
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.db.PAGE_LENGTH
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
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Author::fromResultSet)
            .distinct()

    /**Возвращает список всех авторов**/
    fun listAll(): List<Author> =
        database
            .from(AuthorTable)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name)
            .mapNotNull(Author::fromResultSet)
}
