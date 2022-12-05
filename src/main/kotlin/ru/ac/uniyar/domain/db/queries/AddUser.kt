package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import java.time.LocalDate

class AddUser(
    private val database: Database,
    private val salt: String
) {
    companion object {
        private val sql = """INSERT INTO AUTHOR(NAME,LOGIN,PASSWORD,BIRTH_DATE,REGISTRATION_DATE,ROLE_NAME)
        VALUES(?,?, HASH('SHA3-256',?,10),?, NOW(),?)
        """.trimIndent()
    }

    fun insert(name: String, login: String, password: String, birthDate: LocalDate) =
        database.useConnection { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, name)
                statement.setString(2, login)
                statement.setString(3, password + salt)
                statement.setDate(4, java.sql.Date.valueOf(birthDate))
                statement.setString(5, "USER")
                statement.executeUpdate()
            }
        }
}
