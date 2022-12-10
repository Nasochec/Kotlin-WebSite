package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.tables.FormatTable

class CountFormats(
    private val database: Database
) {
    companion object {
        private val formatCount = org.ktorm.dsl.count(FormatTable.name).aliased("chapterCount")
    }
    fun countAll(): Int =
        database
            .from(FormatTable)
            .select(formatCount)
            .mapNotNull { row -> row[formatCount] }
            .firstOrNull() ?: 0

    fun countFiltered(name: String = ""): Int =
        database
            .from(FormatTable)
            .select(formatCount)
            .where(FormatTable.name.toLowerCase() like "%${name.lowercase()}%")
            .mapNotNull { row -> row[formatCount] }
            .firstOrNull() ?: 0
}
