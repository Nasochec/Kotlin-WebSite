package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.db.tables.ReadHistoryTable

class AddChapterBookmark(val database: Database) {
    fun add(login: String, bookId: Int, number: Int) =
        try {
            database
                .insert(ReadHistoryTable) {
                    set(ReadHistoryTable.userLogin, login)
                    set(ReadHistoryTable.chapterBookId, bookId)
                    set(ReadHistoryTable.chapterNumber, number)
                }
        } catch (_: Exception) {
        }
}
