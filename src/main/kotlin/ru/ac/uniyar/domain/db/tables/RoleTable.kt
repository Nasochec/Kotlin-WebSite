package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

object RoleTable : Table<Nothing>("ROLE") {
    val name = varchar("NAME").primaryKey()
    val can_view_chapters = boolean("CAN_VIEW_CHAPTERS")
    val can_add_books = boolean("CAN_ADD_BOOKS")
    val can_add_chapters = boolean("CAN_ADD_CHAPTERS")
    val can_add_genres = boolean("CAN_ADD_GENRES")
    val can_add_book_formats = boolean("CAN_ADD_BOOK_FORMATS")
    val can_add_ratings = boolean("CAN_ADD_RATINGS")
    val can_change_ratings = boolean("CAN_CHANGE_RATINGS")
}
