package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.results.GenreFullData

class GenreVM(
    val genreFullData: GenreFullData
) : ViewModel {
    val moreBooksUri = "/books?genreName=${genreFullData.genre.name}"
}
