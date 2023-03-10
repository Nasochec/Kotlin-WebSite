package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.GenreTable
import ru.ac.uniyar.domain.entities.Genre

class GetGenres(
    private val database: Database
) {
    fun listAll(): List<Genre> =
        database
            .from(GenreTable)
            .select(GenreTable.name)
            .mapNotNull(Genre::fromResultSet)

    fun list(page: Int, name: String? = null): List<Genre> =
        database
            .from(GenreTable)
            .select(GenreTable.name)
            .where((name == null) or (GenreTable.name.toLowerCase() like "%${name?.lowercase() ?: ""}%"))
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Genre::fromResultSet)
}
