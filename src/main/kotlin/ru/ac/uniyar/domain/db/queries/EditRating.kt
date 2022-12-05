package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.RatingTable

class EditRating(val database: Database) {
    fun edit(oldNeededAge: Int, newNeededAge: Int) =
        database
            .update(RatingTable) {
                set(RatingTable.neededAge, newNeededAge)
                where {
                    RatingTable.neededAge eq oldNeededAge
                }
            }
}
