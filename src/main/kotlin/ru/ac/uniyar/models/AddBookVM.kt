package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.BookFormat
import ru.ac.uniyar.domain.Rars

data class AddBookVM(val form:WebForm = WebForm()) : ViewModel {
    val rarsValues = Rars.values()
    val formatValues = BookFormat.values()
    val name = form.fields["name"]?.get(0)
    val author = form.fields["author"]?.get(0)
    val rars = form.fields["rars"]?.get(0)
    val genre = form.fields["genre"]?.get(0)
    val annotation = form.fields["annotation"]?.get(0)
    val summary = form.fields["summary"]?.get(0)
    val text = form.fields["text"]?.get(0)
    //TODO select rars and fortmat in form
}