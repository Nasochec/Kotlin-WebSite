package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.FORMAT_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.Format

class EditFormatVM(
    val format: Format,
    val form: WebForm = WebForm()
) : ViewModel {
    val formatNameLength = FORMAT_NAME_MAX_LENGTH
}
