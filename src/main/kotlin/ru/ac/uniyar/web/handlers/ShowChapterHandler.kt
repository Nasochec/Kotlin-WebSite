package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Path
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.ChapterVM

fun showChapter(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = handler@{ request ->
    val bookIdPath = Path.of("bookId")
    val idPath = Path.of("id")
    val bookId = bookIdPath(request).toIntOrNull()
    val id = idPath(request).toIntOrNull()
    if (bookId == null || id == null)
        return@handler Response(Status.BAD_REQUEST)
    operationHolder.getChapter.get(bookId, id)?.let { chapter ->
        val book = operationHolder.getBook.get(chapter.bookId)!!
        val author = operationHolder.getAuthor.get(book.authorId)!!
        // для нахождения номера следующей и предыдущей главы сделаны отдельные запросы,
        // поскольку главы могут быть добавлены фрагментами(к примеру есть 1 и 4 главы, а 2 и 3 - нет)
        val nextChapterNumber = operationHolder.getNextChapterNumber.get(chapter.bookId, chapter.number)
        val prevChapterNumber = operationHolder.getPrevChapterNumber.get(chapter.bookId, chapter.number)
        val viewModel = ChapterVM(chapter, book, author, nextChapterNumber, prevChapterNumber)
        Response(Status.OK).with(htmlView of viewModel)
    } ?: Response(Status.BAD_REQUEST)
}
