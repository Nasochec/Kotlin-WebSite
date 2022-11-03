package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.int

/**Линза для получения автора из параметров в URI**/
fun authorIdLens(value: Request) = lensOrNull(
    Query.int().optional("authorId"),
    value
)
