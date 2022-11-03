package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.int
/**Линза для получения номера страницы из параметров в URI**/
fun pageLens(value: Request) = lensOrDefault(
    Query.int().defaulted("page", 1),
    value,
    1
).let { if (it < 1) 1 else it }
