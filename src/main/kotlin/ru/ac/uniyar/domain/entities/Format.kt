package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.db.tables.FormatTable

data class Format(val name: String) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Format? =
            try {
                Format(
                    row[FormatTable.name]!!
                )
            } catch (_: NullPointerException) {
                null
            }
    }

    override fun toString(): String = name
}
