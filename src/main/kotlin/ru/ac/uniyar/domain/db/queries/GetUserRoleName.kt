package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.tables.AuthorTable

class GetUserRoleName(private val database: Database) {
    fun get(login: String): String? {
        return database
            .from(AuthorTable)
            .select(AuthorTable.roleName)
            .where(AuthorTable.login eq login)
            .mapNotNull { row ->
                row[AuthorTable.roleName]!!
            }
            .firstOrNull()
    }
}
