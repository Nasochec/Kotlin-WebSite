package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.FormatTable

class UpdateFormats(private val database: Database) {
    fun edit(oldName: String, newName: String) =
        database
            .update(FormatTable) {
                set(FormatTable.name, newName)
                where { FormatTable.name eq oldName }
            }
}
