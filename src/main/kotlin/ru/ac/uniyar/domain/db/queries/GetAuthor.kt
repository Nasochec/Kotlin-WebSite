package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.db.tables.AuthorTable

class GetAuthor(
    private val database: Database
) {
    /**Возвращает автора с заданным id или null если найти не удалось**/
    fun get(id: Int): Author? =
        database
            .from(AuthorTable)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name)
            .where() {
                AuthorTable.id eq id
            }
            .limit(1)
            .mapNotNull(Author::fromResultSet)
            .firstOrNull()
    /**Возвращает самого нового (самого позже добавленного) автора**/
    fun getNewest():Author? =
        database
            .from(AuthorTable)
            .select(AuthorTable.id, AuthorTable.creationDate, AuthorTable.name)
            .mapNotNull(Author::fromResultSet)
            .lastOrNull()

}
