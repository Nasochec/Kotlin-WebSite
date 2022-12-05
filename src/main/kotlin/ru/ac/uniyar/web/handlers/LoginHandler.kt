package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.SameSite
import org.http4k.core.cookie.cookie
import org.http4k.lens.*
import ru.ac.uniyar.authorization.JwtTools
import ru.ac.uniyar.domain.db.queries.CheckPassword
import ru.ac.uniyar.domain.db.tables.AUTHOR_LOGIN_MAX_LENGTH
import ru.ac.uniyar.models.LoginVM
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showLogin(
    htmlView: ContextAwareViewRenderer,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    Response(Status.OK).with(htmlView(request) of LoginVM(form))
}

fun login(
    htmlView: ContextAwareViewRenderer,
    checkPassword: CheckPassword,
    jwtTools: JwtTools
): HttpHandler =
    handler@{ request ->
        val loginLens = FormField.nonEmptyString().required("login", "Логин пользователя")
        val passwordLens = FormField.nonEmptyString().required("password", "Пароль пользователя")
        val formLens = Body.webForm(
            Validator.Feedback,
            loginLens,
            passwordLens
        ).toLens()
        var form = formLens(request)
        try {
            if (form.errors.isEmpty()) {
                if (loginLens(form).length > AUTHOR_LOGIN_MAX_LENGTH) {
                    val newErrors = form.errors + Invalid(
                        loginLens.meta.copy(
                            description = "Длина логина не должна превышать $AUTHOR_LOGIN_MAX_LENGTH символов."
                        )
                    )
                    form = form.copy(errors = newErrors)
                }
                if (!checkPassword.check(loginLens(form), passwordLens(form))) {
                    val newErrors = form.errors + Invalid(
                        loginLens.meta.copy(
                            description = "Введён неверный логин или пароль."
                        )
                    )
                    form = form.copy(errors = newErrors)
                }
                if (form.errors.isNotEmpty())
                    return@handler showLogin(htmlView, form).invoke(request)
                val cookie = Cookie(
                    "auth_token",
                    jwtTools.createToken(loginLens(form))!!,
                    httpOnly = true,
                    sameSite = SameSite.Strict
                )
                Response(Status.FOUND)
                    .header("location", "/")
                    .cookie(cookie)
            } else {
                showLogin(htmlView, form).invoke(request)
            }
        } catch (_: LensFailure) {
            showLogin(htmlView, form).invoke(request)
        }
    }
