package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.tables.FormatTable

class AddFormat(
    private val database: Database
) {
    fun insert(name: String) =
        database
            .insert(FormatTable) {
                set(FormatTable.name, name)
            }
}
