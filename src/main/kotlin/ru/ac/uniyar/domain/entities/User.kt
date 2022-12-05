package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.AuthorTable
import java.time.LocalDate

data class User(
    val login: String,
    val name: String,
    val birthDate: LocalDate
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): User? =
            try {
                User(
                    row[AuthorTable.login]!!,
                    row[AuthorTable.name]!!,
                    row[AuthorTable.birthDate]!!
                )
            } catch (_: NullPointerException) {
                null
            }
    }
}
