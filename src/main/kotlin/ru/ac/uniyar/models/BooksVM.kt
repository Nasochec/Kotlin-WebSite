package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Book

data class BooksVM(val books : Iterable<IndexedValue<Book>>):ViewModel {

}