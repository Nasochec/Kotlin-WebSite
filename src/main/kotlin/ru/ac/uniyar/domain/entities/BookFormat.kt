package ru.ac.uniyar.domain.entities

/** Формат книги **/
enum class BookFormat(val string: String) {
    HARDCOVER("Книга в твёрдом переплёте"),
    PAPERBACK("Книга в мягком переплёте"),
    WEBBOOK("Книга в электронном формате"),
    ARTICLE("Статья в журнале"),
    WEBARTICKLE("Небольшая статья в интернете")
}
