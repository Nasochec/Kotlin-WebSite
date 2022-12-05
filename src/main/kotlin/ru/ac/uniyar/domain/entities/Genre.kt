package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.GenreTable

data class Genre(
    val name: String
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Genre? =
            try {
                Genre(
                    row[GenreTable.name]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
    override fun toString(): String = name
}
