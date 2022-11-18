package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.BookFullData

data class BookVM(
    val bookFullData: BookFullData,
    val currentPage: Int,
    val currentPageUri: Uri
) : ViewModel {
    val nextPageUri = currentPageUri.removeQuery("page").query("page", (currentPage + 1).toString()).toString()
    val prevPageUri = currentPageUri.removeQuery("page").query("page", (currentPage - 1).toString()).toString()
    val addButtonUri = "/chapter/new?bookId=${bookFullData.book.id}"
    val annotationParagraphs = bookFullData.book.annotation.split("\\n", "\n")
}
