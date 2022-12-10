package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.domain.db.queries.UpdateChapters
import ru.ac.uniyar.domain.db.tables.CHAPTER_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.EditChapterVM
import ru.ac.uniyar.web.lens.path.bookIdPathLens
import ru.ac.uniyar.web.lens.path.chapterNumberPathLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showEditChapter(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getChapter: GetChapter,
    getBook: GetBook,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val number = chapterNumberPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val chapter = getChapter.get(bookId, number, user.login) ?: return@handler Response(Status.BAD_REQUEST)
    val book = getBook.get(bookId) ?: return@handler Response(Status.BAD_REQUEST)
    if (book.authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
    Response(Status.OK).with(htmlView(request) of EditChapterVM(chapter, book, form))
}

fun editChapter(
    htmlView: ContextAwareViewRenderer,
    userLens: BiDiLens<Request, User?>,
    getChapter: GetChapter,
    getBook: GetBook,
    updateChapters: UpdateChapters
): HttpHandler = handler@{ request ->
    val nameLens = FormField.string().required("name", "Заполните название главы")
    val numberLens = FormField.int().required("number", "Заполните номер главы")
    val textLens = FormField.nonEmptyString().required("text", "Заполните текст главы")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens,
        numberLens,
        textLens
    ).toLens()
    var form = formLens(request)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    val bookId = bookIdPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    val number = chapterNumberPathLens(request) ?: return@handler Response(Status.BAD_REQUEST)
    getChapter.get(bookId, number, user.login) ?: return@handler Response(Status.BAD_REQUEST)
    if (getBook.get(bookId)?.authorLogin != user.login) return@handler Response(Status.FORBIDDEN)
    try {
        if (form.errors.isEmpty()) {
            if (numberLens(form) < 1) {
                val newErrors = form.errors + Invalid(
                    numberLens.meta.copy(
                        description = "Указан неверный номер главы. Он должен быть натуральным числом."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (nameLens(form).length > CHAPTER_NAME_MAX_LENGTH) {
                val newErrors = form.errors + Invalid(
                    nameLens.meta.copy(
                        description = "Длина названия главы не должна превышать $CHAPTER_NAME_MAX_LENGTH символов."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (numberLens(form) != number && getChapter.get(bookId, numberLens(form), user.login) != null) {
                val newErrors = form.errors + Invalid(
                    numberLens.meta.copy(
                        description = "Введён некорректный номер главы, глава с таким номером уже существует." +
                            " Попробуйте ввести другой номер главы." +
                            " На странице подробной информации о книге можно увидеть занятые номера глав."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (form.errors.isNotEmpty())
                return@handler showEditChapter(htmlView, userLens, getChapter, getBook, form).invoke(request)
            updateChapters.edit(bookId, number, nameLens(form), textLens(form), numberLens(form))
            Response(Status.FOUND).header("location", "/chapter/$bookId/${numberLens(form)}")
        } else
            showEditChapter(htmlView, userLens, getChapter, getBook, form).invoke(request)
    } catch (_: LensFailure) {
        showEditChapter(htmlView, userLens, getChapter, getBook, form).invoke(request)
    }
}
