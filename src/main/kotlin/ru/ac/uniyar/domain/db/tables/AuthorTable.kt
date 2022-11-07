package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

const val AUTHOR_NAME_MAX_LENGTH = 100

object AuthorTable : Table<Nothing>("AUTHOR") {
    val id = int("ID").primaryKey()
    val name = varchar("NAME")
    val creationDate = datetime("CREATION_DATE")
}
