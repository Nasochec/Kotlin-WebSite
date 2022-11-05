package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
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
            .mapNotNull(Genre::fromResultSet)
            .firstOrNull()
}
