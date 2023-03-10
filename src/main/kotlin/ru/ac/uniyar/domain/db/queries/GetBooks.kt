package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.or
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.entities.Book

class GetBooks(
    private val database: Database
) {
    /**Возвращает список из не более чем 10 книг с применением фильтрации и постраничным выводом**/
    fun list(page: Int, name: String = "", author_login: String? = null, genreName: String? = null): List<Book> =
        database
            .from(BookTable)
            .select()
            .where {
                (BookTable.name.toLowerCase() like "%${name.lowercase()}%") and
                    ((author_login == null) or (BookTable.authorLogin eq (author_login ?: ""))) and
                    ((genreName == null) or (BookTable.genreName.toLowerCase() like "%${genreName?.lowercase() ?: ""}%"))
            }
            .orderBy(BookTable.creationDate.desc())
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Book::fromResultSet)

    /**Возвращает список всех книг**/
    fun listAll(): List<Book> =
        database
            .from(BookTable)
            .select()
            .mapNotNull(Book::fromResultSet)

    /**Возвращает список всех книг одного автора**/
    fun listAll(authorLogin: String): List<Book> =
        database
            .from(BookTable)
            .select()
            .where(BookTable.authorLogin eq authorLogin)
            .mapNotNull(Book::fromResultSet)
}
