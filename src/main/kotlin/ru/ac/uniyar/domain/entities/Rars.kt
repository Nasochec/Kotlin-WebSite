package ru.ac.uniyar.domain.entities

/** Возрастной рейтинг книги **/
enum class Rars(val string: String) {
    BABY("0+"),
    CHILD("6+"),
    TEENAGE("12+"),
    YOUNG("16+"),
    ADULT("18+")
}
