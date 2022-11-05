package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.string

/**Линза для получения имени автора из параметров в URI**/
fun authorNameLens(request: Request) = lensOrDefault(
    Query.string().defaulted("authorName",""),
    request,
    ""
)
