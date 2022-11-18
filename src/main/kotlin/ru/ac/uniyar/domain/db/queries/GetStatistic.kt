package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.count
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.groupBy
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.ChapterTable
import ru.ac.uniyar.domain.db.tables.GenreTable

/**Различные запросы к БД для отображения статистической информации**/
class GetStatistic(
    private val database: Database,
    private val countAuthors: CountAuthors,
    private val countBooks: CountBooks,
    private val countChapters: CountChapters
) {
    private val bookCount = count(BookTable.id).aliased("bookCount")

    /**Возвращает пару - автор написавший наибольшее количество книг и их количество**/
    fun getAuthorWithMostBooks(): Pair<String, Int>? = database
        .from(AuthorTable)
        .leftJoin(BookTable, BookTable.authorId eq AuthorTable.id)
        .select(AuthorTable.id, AuthorTable.name, bookCount)
        .groupBy(AuthorTable.id)
        .orderBy(bookCount.desc())
        .mapNotNull { row: QueryRowSet ->
            val authorName = row[AuthorTable.name]
            val bookNumber = row[bookCount]
            if (authorName == null || bookNumber == null) null
            else Pair(authorName, bookNumber)
        }
        .firstOrNull()

    private val chaperCount = count(ChapterTable.number).aliased("chapterCount")

    /**Возвращает пару - автор написавший наибольшее количество глав и их количество**/
    fun getAuthorWithMostChapters(): Pair<String, Int>? = database
        .from(AuthorTable)
        .leftJoin(BookTable, BookTable.authorId eq AuthorTable.id)
        .leftJoin(ChapterTable, BookTable.id eq ChapterTable.bookId)
        .select(AuthorTable.id, AuthorTable.name, chaperCount)
        .groupBy(AuthorTable.id)
        .orderBy(chaperCount.desc())
        .mapNotNull { row: QueryRowSet ->
            val authorName = row[AuthorTable.name]
            val chapterNumber = row[chaperCount]
            if (authorName == null || chapterNumber == null) null
            else Pair(authorName, chapterNumber)
        }
        .firstOrNull()

    /**Возвращает пару - книга содержащая наибольшее количество глав и их количество**/
    fun getBookWithMostChapters(): Pair<String, Int>? = database
        .from(BookTable)
        .leftJoin(ChapterTable, BookTable.id eq ChapterTable.bookId)
        .select(BookTable.id, BookTable.name, chaperCount)
        .groupBy(BookTable.id)
        .orderBy(chaperCount.desc())
        .mapNotNull { row: QueryRowSet ->
            val bookName = row[BookTable.name]
            val chapterNumber = row[chaperCount]
            if (bookName == null || chapterNumber == null) null
            else Pair(bookName, chapterNumber)
        }
        .firstOrNull()

    /**Возвращает пару - жанр в котором написано наибольшее количество книг и их количество**/
    fun getGenreWithMostBooks(): Pair<String, Int>? = database
        .from(GenreTable)
        .leftJoin(BookTable, BookTable.genreId eq GenreTable.id)
        .select(GenreTable.id, GenreTable.name, bookCount)
        .groupBy(GenreTable.id)
        .orderBy(bookCount.desc())
        .mapNotNull { row: QueryRowSet ->
            val genreName = row[GenreTable.name]
            val bookNumber = row[bookCount]
            if (genreName == null || bookNumber == null) null
            else Pair(genreName, bookNumber)
        }
        .firstOrNull()

    fun countAuthors() = countAuthors.count()
    fun countBooks() = countBooks.count()
    fun countChapters() = countChapters.count()
}
