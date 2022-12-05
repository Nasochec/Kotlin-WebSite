package ru.ac.uniyar.authorization

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.RoleTable

data class Permissions(
    val name: String,
    val can_view_chapters: Boolean = false,
    val can_add_books: Boolean = false,
    val can_add_chapters: Boolean = false,
    val can_add_genres: Boolean = false,
    val can_add_book_formats: Boolean = false,
    val can_add_ratings: Boolean = false,
    val can_change_ratings: Boolean = false
) {
    companion object {
        fun fromQueryRowSet(row: QueryRowSet): Permissions? =
            try {
                Permissions(
                    row[RoleTable.name]!!,
                    row[RoleTable.can_view_chapters]!!,
                    row[RoleTable.can_add_books]!!,
                    row[RoleTable.can_add_chapters]!!,
                    row[RoleTable.can_add_genres]!!,
                    row[RoleTable.can_add_book_formats]!!,
                    row[RoleTable.can_add_ratings]!!,
                    row[RoleTable.can_change_ratings]!!
                )
            } catch (_: NullPointerException) {
                null
            }
    }
}
