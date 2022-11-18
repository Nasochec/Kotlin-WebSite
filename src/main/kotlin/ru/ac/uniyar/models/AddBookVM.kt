package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Author
import ru.ac.uniyar.domain.entities.BookFormat
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.domain.entities.Rars

data class AddBookVM(
    val form: WebForm = WebForm(),
    val errors: List<String>,
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
