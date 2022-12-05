package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.tables.RatingTable

class AddRating(
    private val database: Database
) {
    fun insert(neededAge: Int) =
        database
            .insert(RatingTable) {
                set(RatingTable.neededAge, neededAge)
            }
}
