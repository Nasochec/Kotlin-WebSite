package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import ru.ac.uniyar.domain.db.queries.AddAuthor
import ru.ac.uniyar.domain.db.queries.AddBook
import ru.ac.uniyar.domain.db.queries.AddChapter
import ru.ac.uniyar.domain.db.queries.CountAuthors
import ru.ac.uniyar.domain.db.queries.CountBooks
import ru.ac.uniyar.domain.db.queries.CountChapters
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.GetBooks
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.domain.db.queries.GetStatistic

class OperationHolder(database: Database) {
    val addAuthor = AddAuthor(database)
    val addBook = AddBook(database)
    val addChapter = AddChapter(database)
    private val countAuthors = CountAuthors(database)
    private val countBooks = CountBooks(database)
    private val countChapters = CountChapters(database)
    val getAuthor = GetAuthor(database)
    val getAuthors = GetAuthors(database)
    val getBooks = GetBooks(database)
    val getGenre = GetGenre(database)
    val getGenres = GetGenres(database)
    val statistic = GetStatistic(database, countAuthors, countBooks, countChapters)
    val getBook = GetBook(database, getAuthor, getGenre)
    val getChapter = GetChapter(database, getBook, getAuthor)
}
