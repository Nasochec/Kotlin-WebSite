package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.int

/**Линза для получения книги из параметров в URI**/
fun bookIdLens(request: Request) = lensOrNull(
    Query.int().optional("bookId"),
    request
)
