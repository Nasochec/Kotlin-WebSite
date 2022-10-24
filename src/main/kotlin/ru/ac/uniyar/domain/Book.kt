package ru.ac.uniyar.domain

import java.time.Instant
import java.util.*


data class Book(
    val name: String,
    val author: String,
    val rating: Rars,
    val format: BookFormat,
    val genre: String,
    val annotation: String,
    val summary: String,
    val text: String
) {
    val creationDate: Date = Date.from(Instant.now())
}

//Фозрастной рейтинг книги
enum class Rars(val string: String) {
    BABY("0+"),
    CHILD("6+"),
    TEENAGE("12+"),
    YOUNG("16+"),
    ADULT("18+")
}

//Формат книги
enum class BookFormat(val string: String) {
    HARDCOVER("Книга в твёрдом переплёте"),
    PAPERBACK("Книга в мягком переплёте"),
    WEBBOOK("Книга в электронном формате"),
    ARTICLE("Статья в журнале"),
    WEBARTICKLE("Небольшая статья в интернете")
}