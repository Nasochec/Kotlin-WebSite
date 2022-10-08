package ru.ac.uniyar.domain

import java.time.Instant
import java.util.*


data class Book(
    val name: String,
    val author: String,
    val rating: RARS,
    val format: BOOKFORMAT,
    val genre: String,
    val annotation: String,
    val summary:String,
    val text: String
) {
    val creationDate: Date = Date.from(Instant.now())
}
//class Book {
//    val name:String
//    val author:String
//    val rating:RARS
//    val format:BOOKFORMAT
//    val genre:String
//    val annotation:String
//    val summary:String
//    val text:String
//    constructor(name:String,author:String,rating:RARS,format:BOOKFORMAT,genre:String,annotation: String,text:String){
//
//
//    }
//}
//Возрастная классификация информационной продукции (Russian Age Rating System, RARS)
enum class RARS(val string: String){
    BABY("0+"),
    CHILD("6+"),
    TEENAGE("12+"),
    YOUNG("16+"),
    ADULT("18+")
}
//Ворма книги
enum class BOOKFORMAT(val string:String){
    HARDCOVER("Книга в твёрдом переплёте"),
    PAPERBACK("Книга в мягком переплёте"),
    WEBBOOK("Книга в электронном формате"),
    ARTICLE("Статья в журнале"),
    WEBARTICKLE("Небольшая статья в интернете")
}