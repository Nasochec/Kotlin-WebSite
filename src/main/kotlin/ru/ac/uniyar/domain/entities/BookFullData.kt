package ru.ac.uniyar.domain.entities

data class BookFullData(
    val book: Book,
    val author: Author,
    val genre: Genre,
    val chapters: List<Chapter>
)
