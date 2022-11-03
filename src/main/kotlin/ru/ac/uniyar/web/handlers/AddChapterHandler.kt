package ru.ac.uniyar.web.handlers

import org.http4k.core.*
import org.http4k.lens.*
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.models.AddChapterVM
import ru.ac.uniyar.web.lens.bookIdLens

fun addNewChapter(
    htmlView: BiDiBodyLens<ViewModel>,
    operationHolder: OperationHolder,
    form: WebForm = WebForm()
): HttpHandler = { request ->
    val bookId = bookIdLens(request)
    val books = operationHolder.getBooks.listAll()
    Response(Status.OK).with(htmlView of AddChapterVM(form, books, bookId))
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
            //TODO show errors
            if (operationHolder.getBook.get(bookLens(form)) == null)
                return@handler Response(Status.BAD_REQUEST).body("Указана неверная книга, такой книги не существует!")
            if (operationHolder.getChapter.get(bookLens(form), numberLens(form)) != null)
                return@handler Response(Status.BAD_REQUEST)
                    .body("Указан неверный номер главы. Глава с таким номером а данной книге уже существует!")
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
