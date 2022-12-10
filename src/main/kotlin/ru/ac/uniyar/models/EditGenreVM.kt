package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.GENRE_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.Genre

class EditGenreVM(
    val genre: Genre,
    val form: WebForm = WebForm()
) : ViewModel {
    val genreNameLength = GENRE_NAME_MAX_LENGTH
}
