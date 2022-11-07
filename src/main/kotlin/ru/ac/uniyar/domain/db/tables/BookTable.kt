package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

const val BOOK_NAME_MAX_LENGTH = 100

object BookTable : Table<Nothing>("BOOK") {
    val id = int("ID").primaryKey()
    val name = varchar("NAME")
    val authorId = int("AUTHOR_ID")
    val genreId = int("GENRE_ID")
    val creationDate = datetime("CREATION_DATE")
    val rating = varchar("RATING")
    val format = varchar("FORMAT")
    val annotation = varchar("ANNOTATION")
}
