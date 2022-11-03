package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.tables.AuthorTable
import java.time.LocalDateTime

class AddAuthor(
    private val database: Database
) {
    fun insert(name: String) =
        database
            .insert(AuthorTable) {
                set(AuthorTable.name, name)
                set(AuthorTable.creationDate, LocalDateTime.now())
            }
}
