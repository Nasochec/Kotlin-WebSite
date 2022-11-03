package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.db.PageLenght
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable

class CountAuthors(
    private val database: Database
) {
    fun count(): Int =
        database
            .from(AuthorTable)
            .select()
            .totalRecords
}
