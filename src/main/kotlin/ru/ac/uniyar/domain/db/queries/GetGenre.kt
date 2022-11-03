package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Genre
import ru.ac.uniyar.domain.db.tables.GenreTable

class GetGenre(
    private val database: Database
) {
    fun get(id: Int): Genre? =
        database
            .from(GenreTable)
            .select(GenreTable.id, GenreTable.name)
            .where(GenreTable.id eq id)
            .limit(1)
            .mapNotNull(Genre::fromResultSet)
            .firstOrNull()
}
