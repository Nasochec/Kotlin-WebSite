package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Chapter
import ru.ac.uniyar.domain.Genre

data class BookVM(
    val book: Book,
    val author: Author,
    val genre: Genre,
    val chapters: List<Chapter>,
    val currentPage: Int,
    val currentPageUri: Uri
) : ViewModel {
    val nextPageUri = currentPageUri.removeQuery("page").query("page", (currentPage + 1).toString()).toString()
    val prevPageUri = currentPageUri.removeQuery("page").query("page", (currentPage - 1).toString()).toString()
    val addButtonUri = "/chapter/new?bookId=${book.id}"
    val annotationParagraphs = book.annotation.split("\n")
}
