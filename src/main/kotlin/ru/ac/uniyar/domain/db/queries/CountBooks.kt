package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.count
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.tables.BookTable

class CountBooks(
    private val database: Database
) {
    companion object {
        private val bookCount = count(BookTable.id).aliased("bookCount")
    }
    /**Считает количество всех книг**/
    fun countAll(): Int =
        database
            .from(BookTable)
            .select(bookCount)
            .mapNotNull { row -> row[bookCount] }
            .firstOrNull() ?: 0
    /**Считает количество книг подходящих по параметрам фильтрации**/
    fun countFiltered(name: String = "", author_login: String? = null, genreName: String? = null): Int =
        database
            .from(BookTable)
            .select(bookCount)
            .where {
                (BookTable.name.toLowerCase() like "%${name.lowercase()}%") and
                    ((author_login == null) or (BookTable.authorLogin eq (author_login ?: ""))) and
                    ((genreName == null) or (BookTable.genreName.toLowerCase() like "%${genreName?.lowercase() ?: ""}%"))
            }
            .mapNotNull { row -> row[bookCount] }
            .firstOrNull() ?: 0
    /**Считает количество всех книг заданного автора**/
    fun countAll(authorLogin: String): Int =
        database
            .from(BookTable)
            .select(bookCount)
            .where(BookTable.authorLogin eq authorLogin)
            .mapNotNull { row -> row[bookCount] }
            .firstOrNull() ?: 0
}
