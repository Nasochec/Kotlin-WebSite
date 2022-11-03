package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object GenreTable : Table<Nothing>("GENRE") {
    val id = int("ID").primaryKey()
    val name = varchar("NAME")
}
