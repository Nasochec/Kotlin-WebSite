package ru.ac.uniyar.domain

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.GenreTable

data class Genre(
    val id: Int,
    val name: String
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Genre? =
            try {
                Genre(
                    row[GenreTable.id]!!,
                    row[GenreTable.name]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
