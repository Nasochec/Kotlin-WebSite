package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import ru.ac.uniyar.domain.db.tables.BookTable
import java.time.LocalDateTime

class AddBook(
    private val database: Database
) {
    fun insert(
        name: String,
        authorLogin: String,
        genreName: String,
        neededAge: Int,
        formatName: String,
        annotation: String
    ): Int =
        database
            .insertAndGenerateKey(BookTable) {
                set(BookTable.name, name)
                set(BookTable.authorLogin, authorLogin)
                set(BookTable.genreName, genreName)
                set(BookTable.neededAge, neededAge)
                set(BookTable.formatName, formatName)
                set(BookTable.annotation, annotation)
                set(BookTable.creationDate, LocalDateTime.now())
            } as Int
}
