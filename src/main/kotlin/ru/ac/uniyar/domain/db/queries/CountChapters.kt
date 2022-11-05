package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.ChapterTable

class CountChapters(
    private val database: Database
) {
    fun count(): Int =
        database
            .from(ChapterTable)
            .select()
            .totalRecords
}
