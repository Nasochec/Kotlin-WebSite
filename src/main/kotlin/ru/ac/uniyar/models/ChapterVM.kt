package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Chapter

data class ChapterVM(
    val chapter: Chapter,
    val book: Book,
    val author: Author,
    val nextChapterNumber: Int?,
    val prevChapterNumber: Int?
) : ViewModel {
    val paragraphs = chapter.text.split("\\n","\n")
}
