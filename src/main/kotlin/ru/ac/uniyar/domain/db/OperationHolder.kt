package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import ru.ac.uniyar.domain.db.queries.*

class OperationHolder(database: Database) {
    val addAuthor = AddAuthor(database)
    val addBook = AddBook(database)
    val addChapter = AddChapter(database)
    val countAuthors = CountAuthors(database)
    val countBooks = CountBooks(database)
    val countChapter = CountChapters(database)
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
}
