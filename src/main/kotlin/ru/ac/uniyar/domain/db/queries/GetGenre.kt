package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.queries.results.GenreFullData
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.GenreTable
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre

class GetGenre(
    private val database: Database
) {
    fun get(name: String): Genre? =
        database
            .from(GenreTable)
            .select(GenreTable.name)
            .where(GenreTable.name eq name)
            .mapNotNull(Genre::fromResultSet)
            .firstOrNull()

    fun getFullData(name: String): GenreFullData? {
        val genre = get(name) ?: return null
        return GenreFullData(genre, getGenreBooks(name))
    }

    fun getGenreBooks(genreName: String): List<Book> =
        database
            .from(BookTable)
            .select()
            .where { BookTable.genreName eq genreName }
            .orderBy(BookTable.creationDate.desc())
            .limit(PAGE_LENGTH)
            .mapNotNull(Book::fromResultSet)

    fun getNotNull(name: String): Genre = get(name)!!
}
