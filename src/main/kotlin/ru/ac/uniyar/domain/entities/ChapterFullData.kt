package ru.ac.uniyar.domain.entities

data class ChapterFullData(
    val chapter: Chapter,
    val book: Book,
    val author: Author,
    // для нахождения номера следующей и предыдущей главы сделаны отдельные запросы,
    // поскольку главы могут быть добавлены фрагментами(к примеру есть 1 и 4 главы, а 2 и 3 - нет)
    val nextChapterNumber: Int?,
    val prevChapterNumber: Int?
)
