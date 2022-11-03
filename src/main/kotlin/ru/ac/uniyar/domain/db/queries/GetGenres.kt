package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Genre
import ru.ac.uniyar.domain.db.tables.GenreTable

class GetGenres (
    private val database: Database
) {
    fun listAll(): List<Genre> =
        database
            .from(GenreTable)
            .select(GenreTable.id, GenreTable.name)
            .mapNotNull(Genre::fromResultSet)
}
