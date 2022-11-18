package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.BookTable

class CountBooks(
    private val database: Database
) {
    private val bookCount = org.ktorm.dsl.count(BookTable.id).aliased("bookCount")
    fun count(): Int =
        database
            .from(BookTable)
            .select(bookCount)
            .mapNotNull { row -> row[bookCount] }
            .firstOrNull() ?: 0
}
