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
import ru.ac.uniyar.web.filters.errorRequestFilter
import ru.ac.uniyar.web.filters.permissionFilter
import ru.ac.uniyar.web.handlers.*
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
            "/register" bind Method.POST to addUser(htmlView, operationHolder.addUser),
            "/login" bind Method.GET to showLogin(htmlView),
            "/login" bind Method.POST to login(htmlView, operationHolder.checkPassword, jwtTools),
            "/logout" bind Method.GET to logout(),

            "/statistics" bind Method.GET to showStatistics(htmlView, operationHolder.statistic),

            "/authors" bind Method.GET to showAuthors(
                htmlView,
                operationHolder.getAuthors,
                operationHolder.countAuthors,
                operationHolder.getGenres
            ),
            "/books" bind Method.GET to showBooks(
                htmlView,
                operationHolder.getBooks,
                operationHolder.countBooks,
                operationHolder.getAuthors,
                operationHolder.getGenres
            ),
            "/genres" bind Method.GET to showGenres(
                htmlView,
                operationHolder.getGenres,
                operationHolder.countGenres
            ),
            "/formats" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
                .then(
                    showFormats(
                        htmlView,
                        operationHolder.getFormats,
                        operationHolder.countFormats
                    )
                ),
            "/ratings" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_ratings)
                .then(
                    showRatings(
                        htmlView,
                        operationHolder.getRatings,
                        operationHolder.countRatings
                    )
                ),

            "/book/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_books)
                .then(showAddBook(htmlView, operationHolder)),
            "/book/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_books)
                .then(addBook(userLens, htmlView, operationHolder)),

            "/chapter/new" bind Method.GET to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                .then(showAddChapter(userLens, htmlView, operationHolder)),
            "/chapter/new" bind Method.POST to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                .then(addChapter(userLens, htmlView, operationHolder)),

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

            "/author/{authorLogin}" bind Method.GET to showAuthor(htmlView, operationHolder.getAuthor),
            // "/author/{authorLogin}/edit" bind Method.GET to showEditAuthor(userLens,htmlView),
            "/book/{bookId}" bind Method.GET to showBook(htmlView, userLens, permissionsLens, operationHolder.getBook),
            "/chapter/{bookId}/{number}" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_view_chapters)
                    .then(showChapter(htmlView, userLens, operationHolder.getChapter, operationHolder.addChapterBookmark)),
            "/chapter/{bookId}/{number}/publish" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                    .then(publishChapter(userLens, operationHolder.updateChapters, operationHolder.getBook)),
            "/chapter/{bookId}/{number}/hide" bind Method.GET
                to permissionFilter(permissionsLens, Permissions::can_add_chapters)
                    .then(hideChapter(userLens, operationHolder.updateChapters, operationHolder.getBook)),
            "/genre/{genreName}" bind Method.GET to showGenre(htmlView, operationHolder.getGenre),

//            "/rating/{neededAge}/edit" bind Method.GET
//                    to permissionFilter(permissionsLens, Permissions::can_add_ratings)
//                .then(showEditRating(htmlView, operationHolder.getRating)),
//            "/rating/{neededAge}/edit" bind Method.PUT
//                    to permissionFilter(permissionsLens, Permissions::can_add_ratings)
//                .then(editRating(htmlView, operationHolder.getRating,operationHolder.editRating)),
//            "/rating/{neededAge}" bind Method.GET
//                    to permissionFilter(permissionsLens, Permissions::can_add_ratings)
//                .then(showRating(htmlView, operationHolder.getRating)),
//            "/format/{formatName}" bind Method.GET
//                    to permissionFilter(permissionsLens, Permissions::can_add_book_formats)
//                .then(showFormat(htmlView, operationHolder.getFormat)),

            static(ResourceLoader.Classpath("/ru/ac/uniyar/public"))
        )
    )
