package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.AuthorTable

class UpdateAuthors(val database: Database) {
    fun edit(login: String, newName: String) =
        database
            .update(AuthorTable) {
                set(AuthorTable.name, newName)
                where { AuthorTable.login eq login }
            }
}
