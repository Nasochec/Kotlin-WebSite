package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.db.tables.ChapterTable

class CountChapters(
    private val database: Database
) {
    private val chapterCount = org.ktorm.dsl.count(ChapterTable.number).aliased("chapterCount")
    fun count(): Int =
        database
            .from(ChapterTable)
            .select(chapterCount)
            .mapNotNull { row -> row[chapterCount] }
            .firstOrNull() ?: 0
}
