package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Book


data class BookVM(val book: Book) : ViewModel {
}