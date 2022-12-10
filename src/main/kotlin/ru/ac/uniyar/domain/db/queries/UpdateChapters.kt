package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.db.tables.ChapterTable

class UpdateChapters(private val database: Database) {
    fun publish(bookId: Int, number: Int) =
        database
            .update(ChapterTable) {
                set(ChapterTable.isVisible, true)
                where { (ChapterTable.bookId eq bookId) and (ChapterTable.number eq number) }
            }

    fun hide(bookId: Int, number: Int) =
        database
            .update(ChapterTable) {
                set(ChapterTable.isVisible, false)
                where { (ChapterTable.bookId eq bookId) and (ChapterTable.number eq number) }
            }

    fun edit(bookId: Int, number: Int, newName: String?, newText: String?, newNumber: Int?) =
        database.update(ChapterTable) {
            if (newNumber != null) set(ChapterTable.number, newNumber)
            set(ChapterTable.name, newName)
            if (newText != null) set(ChapterTable.text, newText)
            where { (ChapterTable.bookId eq bookId) and (ChapterTable.number eq number) }
        }
}
