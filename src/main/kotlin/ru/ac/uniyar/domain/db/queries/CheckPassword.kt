package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database

class CheckPassword(private val database: Database, private val salt: String) {
    companion object {
        private val sql = (
            """SELECT Exists(
            SELECT * FROM AUTHOR WHERE LOGIN = ? AND PASSWORD = HASH('SHA3-256',?,10)
            ) as RESULT
            """.trimIndent()
            )
    }
    fun check(login: String, password: String): Boolean =
        database.useConnection { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, login)
                statement.setString(2, password + salt)
                val rez = statement.executeQuery()
                rez?.absolute(1)
                rez?.getBoolean(1) ?: false
            }
        }
}
