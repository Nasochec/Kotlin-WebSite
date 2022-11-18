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
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.GenreTable
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.domain.entities.GenreFullData

class GetGenre(
    private val database: Database
) {
    fun get(id: Int): Genre? =
        database
            .from(GenreTable)
            .select(GenreTable.id, GenreTable.name)
            .where(GenreTable.id eq id)
            .mapNotNull(Genre::fromResultSet)
            .firstOrNull()

    fun getFullData(id: Int): GenreFullData? {
        val genre = get(id) ?: return null
        return GenreFullData(genre, getGenreBooks(id))
    }

    fun getGenreBooks(genreId: Int): List<Book> =
        database
            .from(BookTable)
            .select()
            .where { BookTable.genreId eq genreId }
            .orderBy(BookTable.creationDate.desc())
            .limit(PAGE_LENGTH)
            .mapNotNull(Book::fromResultSet)

    fun getNotNull(id: Int): Genre = get(id)!!
}
