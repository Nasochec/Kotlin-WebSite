package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ReadHistoryTable : Table<Nothing>("READ_HISTORY") {
    val userLogin = varchar("USER_LOGIN").primaryKey()
    val chapterBookId = int("CHAPTER_BOOK_ID").primaryKey()
    val chapterNumber = int("CHAPTER_NUMBER").primaryKey()
}
