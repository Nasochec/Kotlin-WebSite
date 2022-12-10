package ru.ac.uniyar.domain.db

import ru.ac.uniyar.domain.db.queries.GetFormat
import ru.ac.uniyar.domain.db.queries.GetFormats
import ru.ac.uniyar.domain.db.queries.GetGenre
import ru.ac.uniyar.domain.db.queries.GetGenres
import ru.ac.uniyar.domain.db.queries.GetRating
import ru.ac.uniyar.domain.db.queries.GetRatings

/**Класс, хранящий операции для получения данных из таблиц жанров, форматов и возрастных рейтингов.**/
data class GetSimpleTables(
    val getGenre: GetGenre,
    val getGenres: GetGenres,
    val getRating: GetRating,
    val getRatings: GetRatings,
    val getFormat: GetFormat,
    val getFormats: GetFormats
)
