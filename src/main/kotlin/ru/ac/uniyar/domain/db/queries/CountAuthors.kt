package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.db.tables.AuthorTable
import ru.ac.uniyar.domain.db.tables.BookTable

class CountAuthors(
    private val database: Database
) {
    companion object {
        private val authorCount = org.ktorm.dsl.count(AuthorTable.login).aliased("authorCount")
    }
    fun countAll(): Int =
        database
            .from(AuthorTable)
            .select(authorCount)
            .mapNotNull { row -> row[authorCount] }
            .firstOrNull() ?: 0

    /**Считает количество авторов подходящих под параметры фильтрации**/
    fun countFiltered(name: String = "", genreName: String? = null): Int = if (genreName == null)
    // разбиение на получение списка с фильтрацией по жанру и без
    // возникает из-за того, что в варианте с фильтрацией используется leftJoin
    // но оттуда могут возникать повторяющиеся элементы, если этой фильтрации не происходит
        countFiltered(name)
    else
        listWithGenreFilter(name, genreName)

    private fun countFiltered(name: String): Int =
        database
            .from(AuthorTable)
            .select(authorCount)
            .where((AuthorTable.name.toLowerCase() like "%${name.lowercase()}%"))
            .mapNotNull { row -> row[authorCount] }
            .firstOrNull() ?: 0

    private fun listWithGenreFilter(name: String, genreName: String): Int =
        database
            .from(AuthorTable)
            .leftJoin(BookTable, AuthorTable.login eq BookTable.authorLogin)
            .selectDistinct(authorCount)
            .where(
                (AuthorTable.name.toLowerCase() like "%${name.lowercase()}%") and
                    (BookTable.genreName.toLowerCase() like "%${genreName.lowercase()}%")
            )
            .mapNotNull { row -> row[authorCount] }
            .firstOrNull() ?: 0
}
