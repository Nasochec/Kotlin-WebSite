package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.BookTable

class UpdateBooks(
    val database: Database
) {
    fun edit(
        id: Int,
        newName: String? = null,
        newRating: Int? = null,
        newGenre: String? = null,
        newFormat: String? = null
    ) =
        database
            .update(BookTable) {
                if (newName != null) set(BookTable.name, newName)
                if (newRating != null) set(BookTable.neededAge, newRating)
                if (newGenre != null) set(BookTable.genreName, newGenre)
                if (newFormat != null) set(BookTable.formatName, newFormat)
                where { BookTable.id eq id }
            }

    fun editRatingAdmin(id: Int, newRating: Int) =
        database
            .update(BookTable) {
                set(BookTable.neededAge, newRating)
                set(BookTable.neededAgeSetByAdmin, true)
                where { BookTable.id eq id }
            }
}
