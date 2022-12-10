package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.BOOK_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.Format
import ru.ac.uniyar.domain.entities.Genre
import ru.ac.uniyar.domain.entities.Rating

class AddBookVM(
    val form: WebForm = WebForm(),
    val genres: List<Genre>,
    val formats: List<Format>,
    val ratings: List<Rating>
) : ViewModel {
    val rating = form.fields["rating"]?.get(0)?.toIntOrNull()
    val bookNameLength = BOOK_NAME_MAX_LENGTH
}
