package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

const val BOOK_NAME_MAX_LENGTH = 100

object BookTable : Table<Nothing>("BOOK") {
    val id = int("ID").primaryKey()
    val name = varchar("NAME")
    val authorLogin = varchar("AUTHOR_LOGIN")
    val genreName = varchar("GENRE_NAME")
    val creationDate = datetime("CREATION_DATE")
    val neededAge = int("NEEDED_AGE")
    val formatName = varchar("FORMAT_NAME")
    val annotation = varchar("ANNOTATION")
}
