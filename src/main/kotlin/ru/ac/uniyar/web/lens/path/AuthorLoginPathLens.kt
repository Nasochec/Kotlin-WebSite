package ru.ac.uniyar.web.lens.path

import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.lens.nonEmptyString
import ru.ac.uniyar.web.lens.lensOrNull

/**Линза для получения автора из URI**/
fun authorLoginPathLens(request: Request) = lensOrNull(
    Path.nonEmptyString().of("authorLogin"),
    request
)
