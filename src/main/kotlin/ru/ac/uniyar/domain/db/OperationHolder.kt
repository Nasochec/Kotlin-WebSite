package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import ru.ac.uniyar.authorization.JwtTools
import ru.ac.uniyar.domain.db.queries.AddBook
import ru.ac.uniyar.domain.db.queries.AddChapter
import ru.ac.uniyar.domain.db.queries.AddChapterBookmark
import ru.ac.uniyar.domain.db.queries.AddFormat
import ru.ac.uniyar.domain.db.queries.AddGenre
import ru.ac.uniyar.domain.db.queries.AddRating
import ru.ac.uniyar.domain.db.queries.AddUser
import ru.ac.uniyar.domain.db.queries.CheckPassword
import ru.ac.uniyar.domain.db.queries.CountAuthors
import ru.ac.uniyar.domain.db.queries.CountBooks
import ru.ac.uniyar.domain.db.queries.CountChapters
import ru.ac.uniyar.domain.db.queries.CountFormats
import ru.ac.uniyar.domain.db.queries.CountGenres
import ru.ac.uniyar.domain.db.queries.CountRatings
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.domain.db.queries.GetAuthors
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.GetBooks
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.domain.db.queries.GetChapters
import ru.ac.uniyar.domain.db.queries.GetFormat
import ru.ac.uniyar.domain.db.queries.GetFormats
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.domain.db.queries.GetRating
import ru.ac.uniyar.domain.db.queries.GetRatings
import ru.ac.uniyar.domain.db.queries.GetStatistic
import ru.ac.uniyar.domain.db.queries.GetUserByToken
import ru.ac.uniyar.domain.db.queries.GetUserRole
import ru.ac.uniyar.domain.db.queries.GetUserRoleName
import ru.ac.uniyar.domain.db.queries.UpdateAuthors
import ru.ac.uniyar.domain.db.queries.UpdateBooks
import ru.ac.uniyar.domain.db.queries.UpdateChapters
import ru.ac.uniyar.domain.db.queries.UpdateFormats
import ru.ac.uniyar.domain.db.queries.UpdateGenres
import ru.ac.uniyar.domain.db.queries.UpdateRatings

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
    val getRatings = GetRatings(database)
    val getFormats = GetFormats(database)
    val getRating = GetRating(database)
    val getFormat = GetFormat(database)
    val getSimpleTables = GetSimpleTables(getGenre, getGenres, getRating, getRatings, getFormat, getFormats)
    val statistic = GetStatistic(database, countAuthors, countBooks, countChapters)
    val getChapters = GetChapters(database)
    val getBook = GetBook(database, getAuthor, getChapters, countChapters)
    val getChapter = GetChapter(database, getBook, getAuthor)
    val getUserByToken = GetUserByToken(database, jwtTools)
    private val getUserRoleName = GetUserRoleName(database)
    val getUserRole = GetUserRole(database, getUserRoleName)
    val checkPassword = CheckPassword(database, salt)
    val addChapterBookmark = AddChapterBookmark(database)
    val updateChapters = UpdateChapters(database)
    val updateAuthors = UpdateAuthors(database)
    val updateBooks = UpdateBooks(database)
    val updateRatings = UpdateRatings(database)
    val updateFormats = UpdateFormats(database)
    val updateGenres = UpdateGenres(database)
}
