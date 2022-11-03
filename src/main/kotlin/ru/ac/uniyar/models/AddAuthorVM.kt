package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

data class AddAuthorVM(val form:WebForm= WebForm()):ViewModel{
    val name = form.fields["name"]?.get(0)
}
