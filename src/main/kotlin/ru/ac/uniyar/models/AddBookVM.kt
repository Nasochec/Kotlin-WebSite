package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Author
import ru.ac.uniyar.domain.BookFormat
import ru.ac.uniyar.domain.Genre
import ru.ac.uniyar.domain.Rars

data class AddBookVM(
    val form: WebForm = WebForm(),
    val authors: List<Author>,
    val genres: List<Genre>,
    val selectedAuthorId: Int?
) : ViewModel {
    val rarsValues = Rars.values()
    val formatValues = BookFormat.values()
    val name = form.fields["name"]?.get(0)
    val author = form.fields["author"]?.get(0)?.toIntOrNull()
    val rars = form.fields["rars"]?.get(0)
    val format = form.fields["format"]?.get(0)
    val genre = form.fields["genre"]?.get(0)?.toIntOrNull()
    val annotation = form.fields["annotation"]?.get(0)
}
