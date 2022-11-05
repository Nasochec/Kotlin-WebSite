package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import ru.ac.uniyar.domain.db.queries.AddAuthor
import ru.ac.uniyar.domain.db.queries.AddBook
import ru.ac.uniyar.domain.db.queries.AddChapter
import ru.ac.uniyar.domain.db.queries.CountAuthors
import ru.ac.uniyar.domain.db.queries.CountBooks
import ru.ac.uniyar.domain.db.queries.CountChapters
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.domain.db.queries.GetAuthorBooks
import ru.ac.uniyar.domain.db.queries.GetAuthorGenres
import ru.ac.uniyar.domain.db.queries.GetAuthorLastActivity
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.GetBookChapters
import ru.ac.uniyar.domain.db.queries.GetBooks
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.domain.db.queries.GetGenreBooks
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.domain.db.queries.GetNextChapterNumber
import ru.ac.uniyar.domain.db.queries.GetPrevChapterNumber
import ru.ac.uniyar.domain.db.queries.Statistic

class OperationHolder(database: Database) {
    val addAuthor = AddAuthor(database)
    val addBook = AddBook(database)
    val addChapter = AddChapter(database)
    val countAuthors = CountAuthors(database)
    val countBooks = CountBooks(database)
    val countChapters = CountChapters(database)
    val getAuthor = GetAuthor(database)
    val getAuthorBooks = GetAuthorBooks(database)
    val getAuthorGenre = GetAuthorGenres(database)
    val getAuthorLastActivity = GetAuthorLastActivity(database)
    val getAuthors = GetAuthors(database)
    val getBook = GetBook(database)
    val getBookChapters = GetBookChapters(database)
    val getBooks = GetBooks(database)
    val getChapter = GetChapter(database)
    val getGenre = GetGenre(database)
    val getGenreBooks = GetGenreBooks(database)
    val getGenres = GetGenres(database)
    val getNextChapterNumber = GetNextChapterNumber(database)
    val getPrevChapterNumber = GetPrevChapterNumber(database)
    val statistic = Statistic(database)
}
