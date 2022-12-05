package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.tables.AUTHOR_LOGIN_MAX_LENGTH
import ru.ac.uniyar.domain.db.tables.AUTHOR_NAME_MAX_LENGTH

class AddUserVM(
    val form: WebForm = WebForm()
) : ViewModel {
    val userLoginLength = AUTHOR_LOGIN_MAX_LENGTH
    val userNameLength = AUTHOR_NAME_MAX_LENGTH
}
