package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.string

/**Линза для получения названия книги из параметров в URI**/
fun bookNameLens(value: Request) = lensOrDefault(
    Query.string().defaulted("bookName", ""),
    value,
    ""
)
