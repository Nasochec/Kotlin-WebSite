package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.varchar

const val FORMAT_NAME_MAX_LENGTH = 50

object FormatTable : Table<Nothing>("FORMAT") {
    val name = varchar("NAME").primaryKey()
}
