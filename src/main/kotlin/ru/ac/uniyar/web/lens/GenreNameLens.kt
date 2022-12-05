package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.nonEmptyString

/**Линза для получения названия жанра из параметров в URI**/
fun genreNameLens(request: Request) = lensOrNull(
    Query.nonEmptyString().optional("genreName"),
    request
)
