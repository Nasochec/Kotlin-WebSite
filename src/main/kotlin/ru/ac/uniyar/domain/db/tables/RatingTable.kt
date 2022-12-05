package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int

object RatingTable : Table<Nothing>("RATING") {
    val neededAge = int("NEEDED_AGE").primaryKey()
}
