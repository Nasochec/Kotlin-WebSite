package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.GenreFullData

data class GenreVM(
    val genreFullData: GenreFullData
) : ViewModel {
    val moreBooksUri = "/books?genreId=${genreFullData.genre.id}"
}
