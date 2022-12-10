package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.count
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.tables.GenreTable

class CountGenres(
    private val database: Database
) {
    companion object {
        val genreCount = count(GenreTable.name).aliased("genreCount")
    }
    /**Считает количество всех жанров**/
    fun countAll(): Int =
        database
            .from(GenreTable)
            .select(genreCount)
            .mapNotNull { row -> row[genreCount] }
            .firstOrNull() ?: 0
    /**Считает количество жанров подходящих под параметры фильтрации**/
    fun countFiltered(name: String? = null): Int =
        database
            .from(GenreTable)
            .select(genreCount)
            .where((name == null) or (GenreTable.name.toLowerCase() like "%${name?.lowercase() ?: ""}%"))
            .mapNotNull { row -> row[genreCount] }
            .firstOrNull() ?: 0
}
