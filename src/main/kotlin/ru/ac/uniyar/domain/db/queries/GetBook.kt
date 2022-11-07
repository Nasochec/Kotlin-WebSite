package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.db.tables.BookTable

class GetBook(
    private val database: Database
) {
    /**Возвращает книгу с заданным id или null если не найдёт**/
    fun get(id: Int): Book? =
        database
            .from(BookTable)
            .select()
            .where { BookTable.id eq id }
            .limit(1)
            .mapNotNull(Book::fromResultSet)
            .firstOrNull()
    /**Возвращает самую новую (самую позже добавленную) книгу**/
    fun getNewest(): Book? =
        database
            .from(BookTable)
            .select()
            .mapNotNull(Book::fromResultSet)
            .lastOrNull()
}
