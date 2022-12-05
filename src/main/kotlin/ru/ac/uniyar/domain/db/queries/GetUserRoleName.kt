package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.db.tables.AuthorTable

class GetUserRoleName(private val database: Database) {
    fun get(login: String): String? {
        return database
            .from(AuthorTable)
            .select(AuthorTable.roleName)
            .where(AuthorTable.login eq login)
            .map { row ->
                row[AuthorTable.roleName]!!
            }
            .firstOrNull()
    }
}
