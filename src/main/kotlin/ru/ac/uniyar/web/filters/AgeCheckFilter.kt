package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import java.time.LocalDateTime

/**Проверяет можно ли показывать пользователю главу исходя из её возрастного рейтинга.
 * Также учитывается что автору главы её надо отобразить невзирая на возрастной рейтинг.**/
fun ageCheckFilter(
    userLens: BiDiLens<Request, User?>,
    getBook: GetBook
) = Filter { next ->
    handler@{ request ->
        val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
        val book = bookIdPathLens(request)?.let { bookId ->
            getBook.get(bookId)
        } ?: return@handler Response(Status.FORBIDDEN)
        if (user.login != book.authorLogin &&
            user.birthDate.atStartOfDay().plusYears(book.rating.neededAge.toLong()) >= LocalDateTime.now()
        )
            return@handler Response(Status.FORBIDDEN)
        else
            return@handler next(request)
    }
}
