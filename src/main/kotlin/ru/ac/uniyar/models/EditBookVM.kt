package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Book
import ru.ac.uniyar.domain.entities.Format
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.domain.entities.Rating

class EditBookVM(
    val book: Book,
    val genres: List<Genre>,
    val ratings: List<Rating>,
    val formats: List<Format>,
    val form: WebForm = WebForm()
) : ViewModel {
    val rating = form.fields["rating"]?.get(0)?.toIntOrNull()
}
