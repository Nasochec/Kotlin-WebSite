package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.GENRE_NAME_MAX_LENGTH

class AddFormatVM(val form: WebForm) : ViewModel {
    val formatNameLength = GENRE_NAME_MAX_LENGTH
}
