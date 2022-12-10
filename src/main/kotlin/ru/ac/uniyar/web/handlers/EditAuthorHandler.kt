package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.queries.GetAuthor
import ru.ac.uniyar.domain.db.queries.UpdateAuthors
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.EditAuthorVM
import ru.ac.uniyar.web.lens.path.authorLoginPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditAuthor(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getAuthor: GetAuthor,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val authorLogin = authorLoginPathLens(request)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
    val author = getAuthor.get(authorLogin) ?: return@handler Response(Status.BAD_REQUEST)
    Response(Status.OK).with(htmlView(request) of EditAuthorVM(author, form))
}

fun editAuthor(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getAuthor: GetAuthor,
    updateAuthors: UpdateAuthors
): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Заполните ваше имя")
    val formLens = Body.webForm(Validator.Feedback, nameLens).toLens()
    val form = formLens(request)
    val authorLogin = authorLoginPathLens(request)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    if (authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
    try {
        if (form.errors.isEmpty()) {
            getAuthor.get(authorLogin) ?: return@handler Response(Status.BAD_REQUEST)
            updateAuthors.edit(authorLogin, nameLens(form))
            Response(Status.FOUND).header("location", "/author/${user.login}")
        } else
            showEditAuthor(htmlView, userLens, getAuthor, form).invoke(request)
    } catch (_: LensFailure) {
        showEditAuthor(htmlView, userLens, getAuthor, form).invoke(request)
    }
}
