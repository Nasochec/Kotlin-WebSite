package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.db.tables.FormatTable
import ru.ac.uniyar.domain.entities.Format

class GetFormat(
    private val database: Database
) {
    fun get(name: String): Format? =
        database
            .from(FormatTable)
            .select(FormatTable.name)
            .where(FormatTable.name eq name)
            .mapNotNull(Format::fromResultSet)
            .firstOrNull()
    fun getNotNull(name: String): Format = get(name)!!
}
