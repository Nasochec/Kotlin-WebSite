package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.db.tables.CHAPTER_NAME_MAX_LENGTH
import ru.ac.uniyar.models.AddChapterVM
import ru.ac.uniyar.web.lens.bookIdLens

// fun showAddChapterPage()

fun addNewChapter(
    htmlView: BiDiBodyLens<ViewModel>,
    operationHolder: OperationHolder,
    form: WebForm = WebForm(),
    errors: List<String> = listOf()
): HttpHandler = { request ->
    val bookId = bookIdLens(request)
    val books = operationHolder.getBooks.listAll()
    Response(Status.OK).with(htmlView of AddChapterVM(form, errors, books, bookId))
}

fun addChapter(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = handler@{ request ->
    val bookLens = FormField.int().required("book", "id книги")
    val nameLens = FormField.string().required("name", "Название главы")
    val numberLens = FormField.int().required("number", "Номер главы")
    val textLens = FormField.nonEmptyString().required("text", "Текст главы")
    val formLens = Body.webForm(
        Validator.Feedback,
        bookLens,
        nameLens,
        numberLens,
        textLens
    ).toLens()
    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            val errors = mutableListOf<String>()
            if (numberLens(form) < 1)
                errors.add("Указан неверный номер главы. Он должен быть натуральным числом.")
            if (nameLens(form).length > CHAPTER_NAME_MAX_LENGTH)
                errors.add("Длина названия главы не должна превышать $CHAPTER_NAME_MAX_LENGTH символов.")
            if (operationHolder.getBook.get(bookLens(form)) == null)
                errors.add(
                    "Выбрана некорректная книга. Либо список книг пуст, перед добавлением главы," +
                        " добавьте книгу. Либо код страницы был повреждён, перезагрузите страницу."
                )
            if (operationHolder.getChapter.get(bookLens(form), numberLens(form)) != null)
                errors.add(
                    "Введён некорректный номер главы, глава с таким номером уже существует." +
                        " Попробуйте ввести другой номер главы." +
                        " На странице подробной информации о книге можно увидеть занятые номера глав."
                )
            if (errors.isNotEmpty())
                return@handler addNewChapter(htmlView, operationHolder, form, errors).invoke(request)
            operationHolder.addChapter.insert(
                bookLens(form),
                numberLens(form),
                nameLens(form),
                textLens(form)
            )
            val chapter = operationHolder.getChapter.get(bookLens(form), numberLens(form))!!
            Response(Status.FOUND).header("location", "/chapter/${chapter.bookId}/${chapter.number}")
        } else {
            addNewChapter(htmlView, operationHolder, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        addNewChapter(htmlView, operationHolder, form).invoke(request)
    }
}
