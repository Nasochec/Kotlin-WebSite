package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Rating

class EditBookRatingVM(
    val book: Book,
    val ratings: List<Rating>,
    val form: WebForm = WebForm()
) : ViewModel {
    val rating = form.fields["rating"]?.get(0)?.toIntOrNull()
}
