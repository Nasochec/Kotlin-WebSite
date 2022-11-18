package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Book

data class AddChapterVM(
    val form: WebForm = WebForm(),
    val errors: List<String>,
    val books: List<Book>,
    val selectedBookId: Int?
) : ViewModel {
    val name = form.fields["name"]?.get(0)
    val book = form.fields["book"]?.get(0)?.toIntOrNull()
    val number = form.fields["number"]?.get(0)?.toIntOrNull()
    val text = form.fields["text"]?.get(0)
}
