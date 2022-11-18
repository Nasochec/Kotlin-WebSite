package ru.ac.uniyar.web.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Query
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.OperationHolder
import ru.ac.uniyar.domain.db.tables.BOOK_NAME_MAX_LENGTH
import ru.ac.uniyar.domain.entities.BookFormat
import ru.ac.uniyar.domain.entities.Rars
import ru.ac.uniyar.models.AddBookVM

fun addNewBook(
    htmlView: BiDiBodyLens<ViewModel>,
    operationHolder: OperationHolder,
    form: WebForm = WebForm(),
    errors: List<String> = listOf()
): HttpHandler = { request ->
    val authorIdQueryLens = Query.int().optional("authorId")
    val id = authorIdQueryLens(request)
    val authors = operationHolder.getAuthors.listAll()
    val genres = operationHolder.getGenres.listAll()
    Response(Status.OK).with(htmlView of AddBookVM(form, errors, authors, genres, id))
}

fun addBook(htmlView: BiDiBodyLens<ViewModel>, operationHolder: OperationHolder): HttpHandler = handler@{ request ->
    val nameLens = FormField.nonEmptyString().required("name", "Название книги")
    val authorIdLens = FormField.int().required("author", "Автор книги")
    val rarsLens = FormField.nonEmptyString().required("rars", "Возрасной рейтинг книги")
    val formatLens = FormField.nonEmptyString().required("format", "Формат книги")
    val genreIdLens = FormField.int().required("genre", "Жанр книги")
    val annotationLens = FormField.nonEmptyString().required("annotation", "")
    val formLens = Body.webForm(
        Validator.Feedback,
        nameLens,
        authorIdLens,
        rarsLens,
        formatLens,
        genreIdLens,
        annotationLens
    ).toLens()

    val form = formLens(request)
    try {
        if (form.errors.isEmpty()) {
            val errors = mutableListOf<String>()
            if (nameLens(form).length > BOOK_NAME_MAX_LENGTH)
                errors.add("Длина названия книги не должна превышать $BOOK_NAME_MAX_LENGTH символов.")
            if (operationHolder.getAuthor.get(authorIdLens(form)) == null)
                errors.add(
                    "Выбран некорректный автор. Либо список авторов пуст, перед добавлением книги," +
                        " добавьте автора. Либо код страницы был повреждён, перезагрузите страницу."
                )
            if (operationHolder.getGenre.get(genreIdLens(form)) == null)
                errors.add(
                    "Выбран некорректный жанр. Либо список жанров пуст, обратитесь к администратору" +
                        " для добавления. Либо код страницы был повреждён, перезагрузите страницу."
                )
            if (errors.isNotEmpty())
                return@handler addNewBook(htmlView, operationHolder, form, errors).invoke(request)
            operationHolder.addBook.insert(
                nameLens(form),
                authorIdLens(form),
                genreIdLens(form),
                Rars.valueOf(rarsLens(form)),
                BookFormat.valueOf(formatLens(form)),
                annotationLens(form)
            )
            val book = operationHolder.getBook.getNewest()!!
            Response(Status.FOUND).header("location", "/book/${book.id}")
        } else {
            addNewBook(htmlView, operationHolder, form).invoke(request)
        }
    } catch (lf: LensFailure) {
        addNewBook(htmlView, operationHolder, form).invoke(request)
    }
}
