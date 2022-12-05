package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.db.queries.results.ChapterFullData

class ChapterVM(
    val chapterFullData: ChapterFullData
) : ViewModel {
    val paragraphs = chapterFullData.chapter.text.split("\\n", "\n")
}
