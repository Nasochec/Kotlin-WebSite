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
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.selectDistinct
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.entities.Author

class GetAuthors(
    private val database: Database
) {
    /**Возвращает список состоящий из не более 10 асторов, с пименением сортировки**/
    fun list(page: Int, name: String = "", genreName: String? = null): List<Author> = if (genreName == null)
    // разбиение на получение списка с фильтрацией по жанру и без
    // возникает из-за того, что в варианте с фильтрацией используется leftJoin
    // но оттуда могут возникать повторяющиеся элементы, если этой фильтрации не происходит
        list(page, name)
    else
        listWithGenreFilter(page, name, genreName)

    private fun list(page: Int, name: String): List<Author> =
        database
            .from(AuthorTable)
            .select(AuthorTable.login, AuthorTable.registrationDate, AuthorTable.name)
            .where((AuthorTable.name.toLowerCase() like "%${name.lowercase()}%"))
            .orderBy(AuthorTable.registrationDate.desc())
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Author::fromResultSet)

    private fun listWithGenreFilter(page: Int, name: String, genreName: String): List<Author> =
        database
            .from(AuthorTable)
            .leftJoin(BookTable, AuthorTable.login eq BookTable.authorLogin)
            .selectDistinct(AuthorTable.login, AuthorTable.registrationDate, AuthorTable.name, BookTable.genreName)
            .where(
                (AuthorTable.name.toLowerCase() like "%${name.lowercase()}%") and
                    (BookTable.genreName.toLowerCase() like "%${genreName.lowercase()}%")
            )
            .orderBy(AuthorTable.registrationDate.desc())
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Author::fromResultSet)

    /**Возвращает список всех авторов**/
    fun listAll(): List<Author> =
        database
            .from(AuthorTable)
            .select(AuthorTable.login, AuthorTable.registrationDate, AuthorTable.name)
            .mapNotNull(Author::fromResultSet)
}
