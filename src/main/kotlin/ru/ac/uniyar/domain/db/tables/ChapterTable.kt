package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.*

const val CHAPTER_NAME_MAX_LENGTH = 50

object ChapterTable : Table<Nothing>("CHAPTER") {
    val bookId = int("BOOK_ID").primaryKey()
    val number = int("NUMBER").primaryKey()
    val name = varchar("NAME")
    val text = varchar("TEXT")
    val creationDate = datetime("CREATION_DATE")
    val isVisible = boolean("IS_VISIBLE")
}
