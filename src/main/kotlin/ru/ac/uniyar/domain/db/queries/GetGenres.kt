package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.GenreTable
import ru.ac.uniyar.domain.entities.Genre

class GetGenres(
    private val database: Database
) {
    fun listAll(): List<Genre> =
        database
            .from(GenreTable)
            .select(GenreTable.id, GenreTable.name)
            .mapNotNull(Genre::fromResultSet)
}
