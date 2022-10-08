package ru.ac.uniyar.domain

class Books {
    val books = mutableListOf<Book>()
    constructor (list: List<Book>) {
        books.addAll( list.toMutableList())
    }
    constructor (vararg list: Book) {
        books.addAll(list)
    }

    fun addBook(book: Book) = books.add(book)
    fun getBook(index: Int): Book = books[index]
    fun getAllIndexed(): Iterable<IndexedValue<Book>> = books.mapIndexed { index, book -> IndexedValue(index, book) }
}