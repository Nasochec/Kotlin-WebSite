package ru.ac.uniyar.models

import org.http4k.template.ViewModel

/** к именам параметров было применено сокращение:
 * authorWMB = authorWithMostBooks
 * authorWMC = authorWithMostChapters
 * bookWMC = bookWithMostChapters
 * genreWMB = genreEithMostBooks**/
data class StatisticsVM(
    val authorsNumber: Int,
    val booksNumber: Int,
    val chaptersNumber: Int,
    val authorWMB: Pair<String, Int>?,
    val authorWMC: Pair<String, Int>?,
    val bookWMC: Pair<String, Int>?,
    val genreWMB: Pair<String, Int>?
) : ViewModel
