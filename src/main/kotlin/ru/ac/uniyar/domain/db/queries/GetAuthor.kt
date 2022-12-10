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
import ru.ac.uniyar.domain.db.queries.results.AuthorFullData
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.db.tables.GenreTable
import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre
import java.time.LocalDateTime

class GetAuthor(
    private val database: Database
) {
    companion object {
        private val maxCreationDateAlias = max(ChapterTable.creationDate).aliased("maxCreationDate")
    }

    /**Возвращает автора с заданным id или null если найти не удалось**/
    fun get(login: String): Author? =
        database
            .from(AuthorTable)
            .select()
            .where(AuthorTable.login eq login)
            .limit(1)
            .mapNotNull(Author::fromResultSet)
            .firstOrNull()

    fun getNotNull(login: String): Author = get(login)!!

    fun getFullData(login: String): AuthorFullData? {
        val author = get(login) ?: return null
        return AuthorFullData(author, getAuthorBooks(login), getAuthorGenres(login), getAuthorLastActivity(login))
    }

    /**Возвращает 10 последних написанных автором книг**/
    private fun getAuthorBooks(author_login: String): List<Book> =
        database
            .from(BookTable)
            .select()
            .where { BookTable.authorLogin eq author_login }
            .orderBy(BookTable.creationDate.desc())
            .limit(PAGE_LENGTH)
            .mapNotNull(Book::fromResultSet)

    /** Возвращает список жанров, в которых пишет автор**/
    private fun getAuthorGenres(author_login: String): List<Genre> =
        database
            .from(BookTable)
            .leftJoin(GenreTable, BookTable.genreName eq GenreTable.name)
            .selectDistinct(GenreTable.name)
            .where { BookTable.authorLogin eq author_login }
            .mapNotNull(Genre::fromResultSet)

    /**Возвращает дату последней активности автора(дата последней написанной главы)**/
    private fun getAuthorLastActivity(author_login: String): LocalDateTime? = database
        .from(ChapterTable)
        .leftJoin(BookTable, ChapterTable.bookId eq BookTable.id)
        .select(BookTable.authorLogin, maxCreationDateAlias)
        .groupBy(BookTable.authorLogin)
        .where(BookTable.authorLogin eq author_login)
        .mapNotNull { row -> row[maxCreationDateAlias] }.firstOrNull()
}
