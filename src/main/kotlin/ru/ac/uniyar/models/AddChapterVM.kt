package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.CHAPTER_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.Book

class AddChapterVM(
    val form: WebForm = WebForm(),
    val books: List<Book>,
    val selectedBookId: Int?
) : ViewModel {
    val book = form.fields["book"]?.get(0)?.toIntOrNull()
    val chapterNameLength = CHAPTER_NAME_MAX_LENGTH
}
