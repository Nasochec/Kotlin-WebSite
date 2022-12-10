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
import ru.ac.uniyar.domain.db.queries.AddChapter
import ru.ac.uniyar.domain.db.queries.GetBook
import ru.ac.uniyar.domain.db.queries.GetBooks
import ru.ac.uniyar.domain.db.queries.GetChapter
import ru.ac.uniyar.domain.db.tables.CHAPTER_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.User
import ru.ac.uniyar.models.AddChapterVM
import ru.ac.uniyar.web.lens.bookIdQueryLens
import ru.ac.uniyar.web.templates.ContextAwareViewRenderer

fun showAddChapter(
    userLens: BiDiLens<Request, User?>,
    htmlView: ContextAwareViewRenderer,
    getBooks: GetBooks,
    form: WebForm = WebForm()
): HttpHandler = handler@{ request ->
    val bookId = bookIdQueryLens(request)
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    val books = getBooks.listAll(user.login)
    Response(Status.OK).with(htmlView(request) of AddChapterVM(form, books, bookId))
}

fun addChapter(
    userLens: BiDiLens<Request, User?>,
    htmlView: ContextAwareViewRenderer,
    getBooks: GetBooks,
    getChapter: GetChapter,
    getBook: GetBook,
    addChapter: AddChapter
): HttpHandler = handler@{ request ->
    val bookLens = FormField.int().required("book", "Выберите книгу")
    val nameLens = FormField.string().required("name", "Заполните название главы")
    val numberLens = FormField.int().required("number", "Заполните номер главы")
    val textLens = FormField.nonEmptyString().required("text", "Заполните текст главы")
    val formLens = Body.webForm(
        Validator.Feedback,
        bookLens,
        nameLens,
        numberLens,
        textLens
    ).toLens()
    val user = userLens(request) ?: return@handler Response(Status.FORBIDDEN)
    var form = formLens(request)
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
            if (getBook.get(bookLens(form)) == null) {
                val newErrors = form.errors + Invalid(
                    bookLens.meta.copy(
                        description = "Выбрана некорректная книга. Либо список книг пуст, перед добавлением главы," +
                            " добавьте книгу. Либо код страницы был повреждён, перезагрузите страницу."
                    )
                )
                form = form.copy(errors = newErrors)
            }
            if (getChapter.get(bookLens(form), numberLens(form), user.login) != null) {
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
                return@handler showAddChapter(userLens, htmlView, getBooks, form).invoke(request)
            addChapter.insert(
                bookLens(form),
                numberLens(form),
                nameLens(form),
                textLens(form)
            )
            Response(Status.FOUND).header("location", "/chapter/${bookLens(form)}/${numberLens(form)}")
        } else {
            showAddChapter(userLens, htmlView, getBooks, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        showAddChapter(userLens, htmlView, getBooks, form).invoke(request)
    }
}
