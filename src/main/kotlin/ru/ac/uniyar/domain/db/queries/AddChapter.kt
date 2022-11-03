package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.tables.ChapterTable
import java.time.LocalDateTime

class AddChapter(
    private val database: Database
) {
    fun insert(bookId: Int, number: Int, name: String, text: String) =
        database
            .insert(ChapterTable) {
                set(ChapterTable.bookId, bookId)
                set(ChapterTable.name, name)
                set(ChapterTable.number, number)
                set(ChapterTable.text, text)
                set(ChapterTable.creationDate, LocalDateTime.now())
            }
}
