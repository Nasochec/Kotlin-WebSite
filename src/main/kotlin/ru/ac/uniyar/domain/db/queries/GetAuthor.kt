package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.groupBy
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.max
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.selectDistinct
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.db.tables.GenreTable
import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.AuthorFullData
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre
import java.time.LocalDateTime

class GetAuthor(
    private val database: Database
) {
    /**Возвращает автора с заданным id или null если найти не удалось**/
    fun get(id: Int): Author? =
        database
            .from(AuthorTable)
            .select()
            .where {
                AuthorTable.id eq id
            }
            .limit(1)
            .mapNotNull(Author::fromResultSet)
            .firstOrNull()

    fun getNotNull(id: Int): Author = get(id)!!

    /**Возвращает самого нового (самого позже добавленного) автора**/
    fun getNewest(): Author? =
        database
            .from(AuthorTable)
            .select()
            .mapNotNull(Author::fromResultSet)
            .lastOrNull()

    fun getFullData(id: Int): AuthorFullData? {
        val author = get(id) ?: return null
        return AuthorFullData(author, getAuthorBooks(id), getAuthorGenres(id), getAuthorLastActivity(id))
    }

    /**Возвращает 10 последних написанных автором книг**/
    private fun getAuthorBooks(authorId: Int): List<Book> =
        database
            .from(BookTable)
            .select()
            .where { BookTable.authorId eq authorId }
            .orderBy(BookTable.creationDate.desc())
            .limit(PAGE_LENGTH)
            .mapNotNull(Book::fromResultSet)

    /** Возвращает список жанров, в которых пишет автор**/
    private fun getAuthorGenres(authorId: Int): List<Genre> =
        database
            .from(BookTable)
            .leftJoin(GenreTable, BookTable.genreId eq GenreTable.id)
            .selectDistinct(BookTable.authorId, GenreTable.id, GenreTable.name)
            .where { BookTable.authorId eq authorId }
            .mapNotNull(Genre::fromResultSet)
    // .distinct()

    private val maxCreationDateAlias = max(ChapterTable.creationDate).aliased("maxCreationDate")

    /**Возвращает дату последней активности автора(дата последней написанной главы)**/
    private fun getAuthorLastActivity(authorId: Int): LocalDateTime? = database
        .from(ChapterTable)
        .leftJoin(BookTable, ChapterTable.bookId eq BookTable.id)
        .select(BookTable.authorId, maxCreationDateAlias)
        .groupBy(BookTable.authorId)
        .where(BookTable.authorId eq authorId)
        .mapNotNull { row -> row[maxCreationDateAlias] }.firstOrNull()
}
