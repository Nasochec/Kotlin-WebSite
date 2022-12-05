package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

class AddRatingVM(val form: WebForm) : ViewModel {
    val neededAge = form.fields["neededAge"]?.get(0)?.toIntOrNull()
}
