package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.authorization.JwtTools
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.entities.User

class GetUserByToken(private val database: Database, private val jwtTools: JwtTools) {
    fun get(token: String): User? = jwtTools.subject(token)?.let { login ->
        database
            .from(AuthorTable)
            .select(AuthorTable.login, AuthorTable.name, AuthorTable.birthDate)
            .where(AuthorTable.login eq login)
            .mapNotNull(User::fromResultSet)
            .firstOrNull()
    }
}
