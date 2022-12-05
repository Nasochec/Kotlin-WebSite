package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.tables.RatingTable
import ru.ac.uniyar.domain.entities.Rating

class GetRating(
    private val database: Database
) {
    fun get(neededAge: Int): Rating? =
        database
            .from(RatingTable)
            .select(RatingTable.neededAge)
            .where(RatingTable.neededAge eq neededAge)
            .mapNotNull(Rating::fromResultSet)
            .firstOrNull()
    fun getNotNull(neededAge: Int): Rating = get(neededAge)!!
}
