package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.PAGE_LENGTH
import ru.ac.uniyar.domain.db.tables.FormatTable
import ru.ac.uniyar.domain.entities.Format

class GetFormats(
    private val database: Database
) {
    fun listAll(): List<Format> =
        database
            .from(FormatTable)
            .select(FormatTable.name)
            .mapNotNull(Format::fromResultSet)

    fun list(page: Int, name: String = ""): List<Format> =
        database
            .from(FormatTable)
            .select(FormatTable.name)
            .where(FormatTable.name.toLowerCase() like "%${name.lowercase()}%")
            .limit((page - 1) * PAGE_LENGTH, PAGE_LENGTH)
            .mapNotNull(Format::fromResultSet)
}
