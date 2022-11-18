package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.BookFullData
import ru.ac.uniyar.domain.entities.Chapter

class GetBook(
    private val database: Database,
    private val getAuthor: GetAuthor,
    private val getGenre: GetGenre
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

    fun getFullData(id: Int, page: Int): BookFullData? {
        val book = get(id) ?: return null
        return BookFullData(
            book,
            getAuthor.getNotNull(book.authorId),
            getGenre.getNotNull(book.genreId),
            getBookChapters(id, page)
        )
    }

    private fun getBookChapters(bookId: Int, pageNumber: Int): List<Chapter> =
        database
            .from(ChapterTable)
            .select()
            .where { ChapterTable.bookId eq bookId }
            .orderBy(ChapterTable.number.asc())
            .limit((pageNumber - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Chapter::fromResultSet)
}
