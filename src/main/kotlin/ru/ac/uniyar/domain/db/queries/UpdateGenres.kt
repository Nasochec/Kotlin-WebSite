package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.GenreTable

class UpdateGenres(private val database: Database) {
    fun edit(oldName: String, newName: String) =
        database
            .update(GenreTable) {
                set(GenreTable.name, newName)
                where { GenreTable.name eq oldName }
            }
}
