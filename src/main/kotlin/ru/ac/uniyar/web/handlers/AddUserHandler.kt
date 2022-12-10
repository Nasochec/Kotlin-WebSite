package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.localDate
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.queries.AddUser
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.domain.db.tables.AUTHOR_LOGIN_MAX_LENGTH
import ru.ac.uniyar.domain.db.tables.AUTHOR_NAME_MAX_LENGTH
import ru.ac.uniyar.models.AddUserVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun addNewUser(
    htmlView: ContextAwareViewRenderer,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    Response(Status.OK).with(htmlView(request) of AddUserVM(form))
}

fun addUser(
    htmlView: ContextAwareViewRenderer,
    addUser: AddUser,
    getAuthor: GetAuthor
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните имя пользователя")
    val loginLens = FormField.nonEmptyString().required("login", "Заполните логин пользователя")
    val passwordLens = FormField.nonEmptyString().required("password", "Заполните пароль пользователя")
    val passwordAcceptLens =
        FormField.nonEmptyString().required("passwordAccept", "Заполните подтверждение пароля пользователя")
    val birthDateLens = FormField.localDate().required("birthDate", "Заполните дата рождения пользователя")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens,
        loginLens,
        passwordLens,
        passwordAcceptLens,
        birthDateLens
    ).toLens()
    var form = formLens(request)
    try {
        if (form.errors.isEmpty()) { // TODO check login contain only eng letters
            if (nameLens(form).length > AUTHOR_NAME_MAX_LENGTH) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(description = "Длина имени не должна превышать $AUTHOR_NAME_MAX_LENGTH символов.")
                )
                form = form.copy(errors = newErrors)
            }
            if (loginLens(form).length > AUTHOR_LOGIN_MAX_LENGTH) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(description = "Длина логина не должна превышать $AUTHOR_LOGIN_MAX_LENGTH символов.")
                )
                form = form.copy(errors = newErrors)
            }
            if (passwordLens(form) != passwordAcceptLens(form)) {
                val newErrors = form.errors + Invalid(
                    passwordLens.meta.copy(description = "Введённые пароли не совпадают.")
                )
                form = form.copy(errors = newErrors)
            }
            if (getAuthor.get(loginLens(form)) != null) {
                val newErrors = form.errors + Invalid(
                    loginLens.meta.copy(description = "Пользователь с таким логином уже существует, выберите другой.")
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler addNewUser(htmlView, form).invoke(request)
            addUser.insert(
                nameLens(form),
                loginLens(form),
                passwordLens(form),
                birthDateLens(form)
            )
            Response(Status.FOUND).header("location", "/login")
        } else {
            addNewUser(htmlView, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        addNewUser(htmlView, form).invoke(request)
    }
}
