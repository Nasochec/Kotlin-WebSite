package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.RatingTable
import ru.ac.uniyar.domain.entities.Rating

class GetRatings(
    private val database: Database
) {
    fun listAll(): List<Rating> =
        database
            .from(RatingTable)
            .select(RatingTable.neededAge)
            .mapNotNull(Rating::fromResultSet)

    fun list(page: Int): List<Rating> =
        database
            .from(RatingTable)
            .select(RatingTable.neededAge)
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Rating::fromResultSet)
}
