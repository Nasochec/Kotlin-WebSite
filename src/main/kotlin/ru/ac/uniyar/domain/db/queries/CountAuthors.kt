package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.AuthorTable

class CountAuthors(
    private val database: Database
) {
    fun count(): Int =
        database
            .from(AuthorTable)
            .select()
            .totalRecords
}
