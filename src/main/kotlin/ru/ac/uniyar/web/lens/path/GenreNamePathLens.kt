package ru.ac.uniyar.web.lens.path

import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.lens.nonEmptyString
import ru.ac.uniyar.web.lens.lensOrNull

/**Линза для получения жанра из URI**/
fun genreNamePathLens(request: Request) = lensOrNull(
    Path.nonEmptyString().of("genreName"),
    request
)
