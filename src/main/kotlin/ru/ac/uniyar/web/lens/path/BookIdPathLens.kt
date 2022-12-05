package ru.ac.uniyar.web.lens.path

import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.lens.int
import ru.ac.uniyar.web.lens.lensOrNull

/**Линза для получения книги из URI**/
fun bookIdPathLens(request: Request) = lensOrNull(
    Path.int().of("bookId"),
    request
)
