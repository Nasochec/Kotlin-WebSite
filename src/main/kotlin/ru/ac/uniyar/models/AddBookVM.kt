package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.*

class AddBookVM(
    val form: WebForm = WebForm(),
    val genres: List<Genre>,
    val formats: List<Format>,
    val ratings: List<Rating>
) : ViewModel {
    val rating = form.fields["rating"]?.get(0)?.toIntOrNull()
}
