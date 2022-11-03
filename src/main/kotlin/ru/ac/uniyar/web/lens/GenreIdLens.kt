package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.int

/**Линза для получения жанра из параметров в URI**/
fun genreIdLens(value: Request) = lensOrNull(
    Query.int().optional("genreId"),
    value
)
