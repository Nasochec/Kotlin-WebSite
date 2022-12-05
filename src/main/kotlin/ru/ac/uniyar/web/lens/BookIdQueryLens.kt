package ru.ac.uniyar.web.lens

import org.http4k.core.Request
import org.http4k.lens.Query
import org.http4k.lens.int

fun bookIdQueryLens(request: Request) = lensOrNull(
    Query.int().optional("bookId"),
    request
)
