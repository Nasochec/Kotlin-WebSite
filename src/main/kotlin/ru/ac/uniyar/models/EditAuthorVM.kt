package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.AUTHOR_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.Author

class EditAuthorVM(val author: Author, val form: WebForm = WebForm()) : ViewModel {
    val userNameLength = AUTHOR_NAME_MAX_LENGTH
    val name = form.fields["name"]?.get(0)
}
