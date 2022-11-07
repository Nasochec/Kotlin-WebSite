package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable

class GetAuthors(
    private val database: Database
) {
    /**Возвращает список состоящий из не более 10 асторов, с пименением сортировки**/
    fun list(page: Int, name: String = "", genreId: Int? = null): List<Author> = if (genreId == null)
    //разбиение на получение списка с фильтрацией по жанру и без
    //возникает из-за того, что в варианте с фильтрацией используется leftJoin
    //но оттуда могут возникать повторяющиеся элементы, если этой фильтрации не происходит
        list(page, name)
    else
        listWithGenreFilter(page, name, genreId)

    private fun list(page: Int, name: String): List<Author> =
        database
            .from(AuthorTable)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name)
            .where { (AuthorTable.name.toLowerCase() like "%${name.lowercase()}%") }
            .orderBy(AuthorTable.creationDate.desc())
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Author::fromResultSet)


    private fun listWithGenreFilter(page: Int, name: String, genreId: Int? = null): List<Author> =
        database
            .from(AuthorTable)
            .leftJoin(BookTable, AuthorTable.id eq BookTable.authorId)
            .selectDistinct(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name, BookTable.genreId)
            .where {
                (AuthorTable.name.toLowerCase() like "%${name.lowercase()}%") and
                    ((genreId == null) or (BookTable.genreId eq (genreId ?: 0)))
            }
            .orderBy(AuthorTable.creationDate.desc())
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Author::fromResultSet)

    /**Возвращает список всех авторов**/
    fun listAll(): List<Author> =
        database
            .from(AuthorTable)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name)
            .mapNotNull(Author::fromResultSet)
}
