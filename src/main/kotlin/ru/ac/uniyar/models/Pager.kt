package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery
import ru.ac.uniyar.domain.db.PAGE_LENGTH

class Pager(
    val currentPage: Int,
    val currentPageUri: Uri,
    val numOfItems: Int
) {
    val haveNextPage = currentPage * PAGE_LENGTH < numOfItems
    val havePrevPage = currentPage > 1

    val nextPageUri = currentPageUri.removeQuery("page").query("page", (currentPage + 1).toString()).toString()
    val prevPageUri = currentPageUri.removeQuery("page").query("page", (currentPage - 1).toString()).toString()
}
