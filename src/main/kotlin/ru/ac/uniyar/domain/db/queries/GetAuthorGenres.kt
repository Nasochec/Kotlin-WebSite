package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Genre
import ru.ac.uniyar.domain.db.tables.BookTable
import ru.ac.uniyar.domain.db.tables.GenreTable

class GetAuthorGenres(
    private val database: Database
) {
    /** Возвращает **/
    fun list(authorId: Int): List<Genre> =
        database
            .from(BookTable)
            .leftJoin(GenreTable, BookTable.genreId eq GenreTable.id)
            .select(BookTable.authorId, GenreTable.id, GenreTable.name)
            .where { BookTable.authorId eq authorId }
            .mapNotNull(Genre::fromResultSet)
            .distinct()
}
