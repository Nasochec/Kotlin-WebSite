package ru.ac.uniyar.web.lens.path

import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.lens.int
import ru.ac.uniyar.web.lens.lensOrNull

/**Линза для получения номера главы из URI**/
fun chapterNumberPathLens(request: Request) = lensOrNull(
    Path.int().of("number"),
    request
)
