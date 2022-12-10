package ru.ac.uniyar.web.routers

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import ru.ac.uniyar.authorization.JwtTools
import ru.ac.uniyar.authorization.Permissions
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.web.filters.ageCheckFilter
import ru.ac.uniyar.web.filters.errorRequestFilter
import ru.ac.uniyar.web.filters.permissionFilter
import ru.ac.uniyar.web.handlers.addBook
import ru.ac.uniyar.web.handlers.addChapter
import ru.ac.uniyar.web.handlers.addFormat
import ru.ac.uniyar.web.handlers.addGenre
import ru.ac.uniyar.web.handlers.addNewUser
import ru.ac.uniyar.web.handlers.addRating
import ru.ac.uniyar.web.handlers.addUser
import ru.ac.uniyar.web.handlers.editAuthor
import ru.ac.uniyar.web.handlers.editBook
import ru.ac.uniyar.web.handlers.editBookRating
import ru.ac.uniyar.web.handlers.editChapter
import ru.ac.uniyar.web.handlers.editFormat
import ru.ac.uniyar.web.handlers.editGenre
import ru.ac.uniyar.web.handlers.editRating
import ru.ac.uniyar.web.handlers.hidePublishChapter
import ru.ac.uniyar.web.handlers.login
import ru.ac.uniyar.web.handlers.logout
import ru.ac.uniyar.web.handlers.ping
import ru.ac.uniyar.web.handlers.showAddBook
import ru.ac.uniyar.web.handlers.showAddChapter
import ru.ac.uniyar.web.handlers.showAddFormat
import ru.ac.uniyar.web.handlers.showAddGenre
import ru.ac.uniyar.web.handlers.showAddRating
import ru.ac.uniyar.web.handlers.showAuthor
import ru.ac.uniyar.web.handlers.showAuthors
import ru.ac.uniyar.web.handlers.showBook
import ru.ac.uniyar.web.handlers.showBooks
import ru.ac.uniyar.web.handlers.showChapter
import ru.ac.uniyar.web.handlers.showEditAuthor
import ru.ac.uniyar.web.handlers.showEditBook
import ru.ac.uniyar.web.handlers.showEditBookRating
import ru.ac.uniyar.web.handlers.showEditChapter
import ru.ac.uniyar.web.handlers.showEditFormat
import ru.ac.uniyar.web.handlers.showEditGenre
import ru.ac.uniyar.web.handlers.showEditRating
import ru.ac.uniyar.web.handlers.showFormats
import ru.ac.uniyar.web.handlers.showGenre
import ru.ac.uniyar.web.handlers.showGenres
import ru.ac.uniyar.web.handlers.showLogin
import ru.ac.uniyar.web.handlers.showMainPage
import ru.ac.uniyar.web.handlers.showRatings
import ru.ac.uniyar.web.handlers.showStatistics
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun app(
    operationHolder: OperationHolder,
    htmlView: ContextAwareViewRenderer,
    jwtTools: JwtTools,
    userLens: BiDiLens<Request, User?>,
    permissionsLens: RequestContextLens<Permissions>
): HttpHandler =
    errorRequestFilter(htmlView).then(
        routes(
            "/" bind Method.GET to showMainPage(htmlView),

            "/ping" bind Method.GET to ping(),

            "/register" bind Method.GET to addNewUser(htmlView),
            "/register" bind Method.POST to addUser(htmlView, operationHolder.addUser, operationHolder.getAuthor),
            "/login" bind Method.GET to showLogin(htmlView),
            "/login" bind Method.POST to login(htmlView, operationHolder.checkPassword, jwtTools),
            "/logout" bind Method.GET to logout(),

            "/statistics" bind Method.GET to showStatistics(htmlView, operationHolder.statistic),

            "/authors" bind Method.GET to showAuthors(
                htmlView, operationHolder.getAuthors,
                operationHolder.countAuthors, operationHolder.getGenres
            ),
            "/books" bind Method.GET to showBooks(
                htmlView, operationHolder.getBooks, operationHolder.countBooks,
                operationHolder.getAuthors, operationHolder.getGenres
            ),
            "/genres" bind Method.GET to showGenres(htmlView, operationHolder.getGenres, operationHolder.countGenres),
            "/formats" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
                .then(showFormats(htmlView, operationHolder.getFormats, operationHolder.countFormats)),
            "/ratings" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_ratings)
                .then(showRatings(htmlView, operationHolder.getRatings, operationHolder.countRatings)),

            "/book/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_books)
                .then(showAddBook(htmlView, operationHolder.getSimpleTables)),
            "/book/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_books)
                .then(addBook(userLens, htmlView, operationHolder.addBook, operationHolder.getSimpleTables)),

            "/chapter/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                .then(showAddChapter(userLens, htmlView, operationHolder.getBooks)),
            "/chapter/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                .then(
                    addChapter(
                        userLens, htmlView, operationHolder.getBooks, operationHolder.getChapter,
                        operationHolder.getBook, operationHolder.addChapter
                    )
                ),

            "/genre/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_genres)
                .then(showAddGenre(htmlView)),
            "/genre/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_genres)
                .then(addGenre(htmlView, operationHolder.addGenre, operationHolder.getGenre)),

            "/format/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
                .then(showAddFormat(htmlView)),
            "/format/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
                .then(addFormat(htmlView, operationHolder.addFormat, operationHolder.getFormat)),

            "/rating/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_ratings)
                .then(showAddRating(htmlView)),
            "/rating/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_ratings)
                .then(addRating(htmlView, operationHolder.addRating, operationHolder.getRating)),

            "/author/{authorLogin}/edit" bind Method.GET
                to showEditAuthor(htmlView, userLens, operationHolder.getAuthor),
            "/author/{authorLogin}/edit" bind Method.POST
                to editAuthor(htmlView, userLens, operationHolder.getAuthor, operationHolder.updateAuthors),
            "/author/{authorLogin}" bind Method.GET to showAuthor(htmlView, operationHolder.getAuthor),

            "/book/{bookId}/edit" bind Method.GET
                to showEditBook(htmlView, userLens, operationHolder.getBook, operationHolder.getSimpleTables),
            "/book/{bookId}/edit" bind Method.POST
                to editBook(
                    htmlView, userLens, permissionsLens, operationHolder.getBook,
                    operationHolder.getSimpleTables, operationHolder.updateBooks
                ),
            "/book/{bookId}/edit_rating" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_change_ratings)
                    .then(showEditBookRating(htmlView, operationHolder.getBook, operationHolder.getRatings)),
            "/book/{bookId}/edit_rating" bind Method.POST
                to permissionFilter(permissionsLens, Permissions::can_change_ratings)
                    .then(
                        editBookRating(
                            htmlView, operationHolder.getBook, operationHolder.getRating,
                            operationHolder.getRatings, operationHolder.updateBooks
                        )
                    ),
            "/book/{bookId}" bind Method.GET to showBook(htmlView, userLens, operationHolder.getBook),

            "/chapter/{bookId}/{number}/edit" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                    .then(showEditChapter(htmlView, userLens, operationHolder.getChapter, operationHolder.getBook)),
            "/chapter/{bookId}/{number}/edit" bind Method.POST
                to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                    .then(
                        editChapter(
                            htmlView, userLens, operationHolder.getChapter,
                            operationHolder.getBook, operationHolder.updateChapters
                        )
                    ),
            "/chapter/{bookId}/{number}" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_view_chapters)
                    .then(ageCheckFilter(userLens, operationHolder.getBook))
                    .then(showChapter(htmlView, userLens, operationHolder.getChapter, operationHolder.addChapterBookmark)),
            "/chapter/{bookId}/{number}" bind Method.POST // TODO age filter and remove response forbidden form all handlers
                to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                    .then(hidePublishChapter(userLens, operationHolder.updateChapters, operationHolder.getBook)),

            "/genre/{genreName}/edit" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_add_genres)
                    .then(showEditGenre(htmlView, operationHolder.getGenre)),
            "/genre/{genreName}/edit" bind Method.POST
                to permissionFilter(permissionsLens, Permissions::can_add_genres)
                    .then(editGenre(htmlView, operationHolder.getGenre, operationHolder.updateGenres)),
            "/genre/{genreName}" bind Method.GET to showGenre(htmlView, operationHolder.getGenre),

            "/rating/{neededAge}/edit" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_add_ratings)
                    .then(showEditRating(htmlView, operationHolder.getRating)),
            "/rating/{neededAge}/edit" bind Method.POST
                to permissionFilter(permissionsLens, Permissions::can_add_ratings)
                    .then(editRating(htmlView, operationHolder.getRating, operationHolder.updateRatings)),

            "/format/{formatName}/edit" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
                    .then(showEditFormat(htmlView, operationHolder.getFormat)),
            "/format/{formatName}/edit" bind Method.POST
                to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
                    .then(editFormat(htmlView, operationHolder.getFormat, operationHolder.updateFormats)),

            static(ResourceLoader.Classpath("/ru/ac/uniyar/public"))
        )
    )
