package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.tables.GenreTable

class AddGenre(
    private val database: Database
) {
    fun insert(name: String) =
        database
            .insert(GenreTable) {
                set(GenreTable.name, name)
            }
}
