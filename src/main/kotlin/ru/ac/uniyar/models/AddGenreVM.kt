package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.GENRE_NAME_MAX_LENGTH

class AddGenreVM(val form: WebForm) : ViewModel {
    val genreNameLength = GENRE_NAME_MAX_LENGTH
}
