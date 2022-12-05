package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import ru.ac.uniyar.authorization.JwtTools
import ru.ac.uniyar.domain.db.queries.*

class OperationHolder(database: Database, salt: String, jwtTools: JwtTools) {
    val addUser = AddUser(database, salt)
    val addBook = AddBook(database)
    val addChapter = AddChapter(database)
    val addGenre = AddGenre(database)
    val addFormat = AddFormat(database)
    val addRating = AddRating(database)
    val countAuthors = CountAuthors(database)
    val countBooks = CountBooks(database)
    val countGenres = CountGenres(database)
    val countChapters = CountChapters(database)
    val countFormats = CountFormats(database)
    val countRatings = CountRatings(database)
    val getAuthor = GetAuthor(database)
    val getAuthors = GetAuthors(database)
    val getBooks = GetBooks(database)
    val getGenre = GetGenre(database)
    val getGenres = GetGenres(database)
    val statistic = GetStatistic(database, countAuthors, countBooks, countChapters)
    val getChapters = GetChapters(database)
    val getBook = GetBook(database, getAuthor, getChapters, countChapters)
    val getChapter = GetChapter(database, getBook, getAuthor)
    val getUserByToken = GetUserByToken(database, jwtTools)
    private val getUserRoleName = GetUserRoleName(database)
    val getUserRole = GetUserRole(database, getUserRoleName)
    val checkPassword = CheckPassword(database, salt)
    val getRatings = GetRatings(database)
    val getFormats = GetFormats(database)
    val getRating = GetRating(database)
    val getFormat = GetFormat(database)
    val addChapterBookmark = AddChapterBookmark(database)
    val updateChapters = UpdateChapters(database)
}
