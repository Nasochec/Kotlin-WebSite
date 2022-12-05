package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.varchar

const val GENRE_NAME_MAX_LENGTH = 50
object GenreTable : Table<Nothing>("GENRE") {
    val name = varchar("NAME").primaryKey()
}
