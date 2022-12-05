package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.results.AuthorFullData

class AuthorVM(
    val authorFullData: AuthorFullData
) : ViewModel {
    val moreBooksUri = "/books?authorLogin=${authorFullData.author.login}"
}
