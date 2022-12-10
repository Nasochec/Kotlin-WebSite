package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Rating

class EditRatingVM(val rating: Rating, val form: WebForm = WebForm()) : ViewModel {
    val neededAge = form.fields["neededAge"]?.get(0)?.toIntOrNull()
}
