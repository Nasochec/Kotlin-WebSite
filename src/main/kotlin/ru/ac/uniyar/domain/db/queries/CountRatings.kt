package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.count
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.RatingTable

class CountRatings(
    private val database: Database
) {
    companion object {
        val ratingCount = count(RatingTable.neededAge).aliased("ratingCount")
    }
    fun countAll(): Int =
        database
            .from(RatingTable)
            .select(ratingCount)
            .mapNotNull { row -> row[ratingCount] }
            .firstOrNull() ?: 0

    fun count(): Int =
        database
            .from(RatingTable)
            .select(ratingCount)
            .mapNotNull { row -> row[ratingCount] }
            .firstOrNull() ?: 0
}
