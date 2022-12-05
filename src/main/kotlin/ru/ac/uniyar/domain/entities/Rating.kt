package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.RatingTable

data class Rating(val neededAge: Int) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Rating? =
            try {
                Rating(
                    row[RatingTable.neededAge]!!
                )
            } catch (_: NullPointerException) {
                null
            }
    }

    override fun toString(): String = "$neededAge+"
}
