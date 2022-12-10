package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.CHAPTER_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Chapter

class EditChapterVM(
    val chapter: Chapter,
    val book: Book,
    val form: WebForm = WebForm()
) : ViewModel {
    val chapterNameLength = CHAPTER_NAME_MAX_LENGTH
}
