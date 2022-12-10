package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.queries.results.BookFullData
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.entities.Book

class GetBook(
    private val database: Database,
    private val getAuthor: GetAuthor,
    private val getChapters: GetChapters,
    private val countChapters: CountChapters,
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

    fun getNotNull(id: Int) = get(id)!!

    /**Возвращает самую новую (самую позже добавленную) книгу**/
    fun getNewest(): Book? =
        database
            .from(BookTable)
            .select()
            .mapNotNull(Book::fromResultSet)
            .lastOrNull()

    /**Возвращает книгу, её автора, список её глав и их количество, с учётом что скрытые главы надо отображать только автору.**/
    fun getFullData(id: Int, page: Int, userLogin: String?): BookFullData? {
        val book = get(id) ?: return null
        return BookFullData(
            book,
            getAuthor.getNotNull(book.authorLogin),
            getChapters.list(page, id, userLogin),
            countChapters.count(id, userLogin)
        )
    }
}
