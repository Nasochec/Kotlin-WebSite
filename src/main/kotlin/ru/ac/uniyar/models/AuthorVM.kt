package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.AuthorFullData

data class AuthorVM(
    val authorFullData: AuthorFullData
) : ViewModel {
    val moreBooksUri = "/books?authorId=${authorFullData.author.id}"
    val addBookUri = "/book/new?authorId=${authorFullData.author.id}"
}
