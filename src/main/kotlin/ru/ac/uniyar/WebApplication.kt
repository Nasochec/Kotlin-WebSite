package ru.ac.uniyar

import org.http4k.core.*
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.*
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import org.http4k.template.viewModel
import ru.ac.uniyar.domain.BOOKFORMAT
import ru.ac.uniyar.domain.Book
import ru.ac.uniyar.domain.Books
import ru.ac.uniyar.domain.RARS
import ru.ac.uniyar.models.*


fun  ping(): HttpHandler = {Response(OK).body("pong")}

fun getMainPage(renderer:TemplateRenderer): HttpHandler = {
    // Создать модель
    val viewModel = mainPage(0)
    // Отобразить модель в шаблоне src/test/resources/Person.peb
    val view = Body.viewModel(renderer, TEXT_HTML).toLens()
    Response(OK).with(view of viewModel)
}

fun showAllBooks(renderer:TemplateRenderer,books:Books):HttpHandler={
    val view = Body.viewModel(renderer, TEXT_HTML).toLens()
    val viewModel = BooksVM(books.getAllIndexed())
    Response(OK) .with(view of viewModel)
}

fun showBookIndex(renderer: TemplateRenderer,books: Books):HttpHandler={request->
    val index = request.path("index").orEmpty().toIntOrNull()
    if(index==null || index <0 || index >= books.books.size)
        Response(NOT_FOUND)
    else {
        val viewModel = BookVM(books.getBook(index), index)
        val view = Body.viewModel(renderer, TEXT_HTML).toLens()
        Response(OK).with(view of viewModel)
    }
}

fun addNewBook(renderer: TemplateRenderer):HttpHandler ={
    val viewModel = AddBookVM(0);
    val view = Body.viewModel(renderer, TEXT_HTML).toLens()
    Response(OK).with(view of viewModel)
}

fun addBook(renderer: TemplateRenderer,books: Books):HttpHandler ={request->
    books.addBook(Book(request.form("name").orEmpty(),
        request.form("author").orEmpty(),
        RARS.valueOf(request.form("rars").orEmpty()),
        BOOKFORMAT.valueOf(request.form("format").orEmpty()),
        request.form("genre").orEmpty(),
        request.form("annotation").orEmpty(),
        request.form("summary").orEmpty(),
        request.form("text").orEmpty()
    ))
    Response(FOUND).header("location","/books/" + (books.books.size-1))
}

fun app(renderer: TemplateRenderer,books:Books): HttpHandler =
    routes(
        "/ping" bind GET to ping(),

        "/" bind GET to getMainPage(renderer),

        "/books" bind GET to showAllBooks(renderer, books),

        "/books/new" bind GET to addNewBook(renderer),

        "/books/new" bind Method.POST to addBook(renderer,books),

        "/books/{index}" bind GET to showBookIndex(renderer, books),



        "/templates/pebble" bind GET to {
            val view = Body.viewModel(renderer, TEXT_HTML).toLens()
            val viewModel = PebbleViewModel("Hello there!")
            Response(OK).with(view of viewModel)
        },

        "test" bind GET to {
            val g=books.getAllIndexed()
            Response(OK).body( RARS.valueOf("BABY").toString() )
        },

        "/testing/kotest" bind GET to { request ->
            Response(OK).body("Echo '${request.bodyString()}'")
        },
        static(ResourceLoader.Classpath("/ru/ac/uniyar/public"))
    )


fun main() {
    val books = Books(Book("Тестовая книга","Артемий Гладков",RARS.TEENAGE,BOOKFORMAT.ARTICLE,"Psycodelic","Важная книга, для проверки работоспособности программы.","Если коротко, то книга очень интересная.","Текст этой важной книги"),
    Book("Хоп-механика","Мирон Янович",RARS.ADULT,BOOKFORMAT.WEBARTICKLE,"реп","перечень важных фактов про самиздат","самиздат - важная вещь","[Интро]\n" +
            "Лапы к самиздату тянут магистраты и Дяди Стёпы\nЧитающий еврей, но не Матисьяху\nДвигается по галактике автостопом\nДайте пройти, кидайте в шляпу\nКредитные карты, дукаты, злоты\nЕсли водится монет, как у нумизмата\nЕсли ни гроша — напевайте строки\n[Куплет 1]\n" +
            "Жить — это про фарш, про тесто, слои лазаньи\nПеречеркни мои болезни, Демна Гвасалия\nЖив для противостояния угасанию\nЖид — не ортодоксальный, а парадоксальный\nБартер: за лечение в Аркхеме — Пятый Картер\nЖёлтый символ на пиджаке — не эмблема Carhartt\nТьма египетская над бездной, крещение анхом\nБатя возвращается трезвым, в руке буханка\n" +
            "Богу дал, я стереотип о селебрити\nГоды опытов, как в себя прийти, чтоб к себе прийти\nВидно, слиплись от крови глаза Гестапо:\nВот кто поднял выразительно бровь, не Азат Мифтахов\nМузу развезло, инструктировать некому\nПофиг, текст — ремесло, сел инкрустировать, эй\nЯ не пропустил ничего, веками так\nКурёхин спит, на сцене ослы, это Хоп-механика\n\n" +
            "[Припев]\nХор свыше\nПод питчем\nМёртв Ницше\nБог дышит\nЧёрт брешет\nКонтора пишет\nНон-фикшн\nOG shit\nМой слог движет\nСквозь смог рикшей\nСок слов выжал\nИзвёл строф жижу\nСтёр строк больше\nЧем выдал Джон Гришам\nНос тёр жёстче\nРазве что Жижек\n[Куплет 2]\nКогда рвало крышу — затворничал, как Бобби Фишер\nДо сих пор никто из игроков не смог заполнить нишу\n" +
            "Пропасть между Gorillaz и Girls'n'Boys\nЯ сбитый лётчик — так творить начал Йозеф Бойс\nС мылом голяк, петля слаба и соскользнула\nЭто знак, брат, ещё рано умирать, слезай со стула\nИгра бедна текстовиками и соснула\nПусть я в морге, но читаю — Zillakami и Sosmula\nТ-Т-Там где-то д-д-дарк техно\nСмерть на рейве, сет в Алеппо\nГрад с неба идёт, как глиттер\n" +
            "Аве Мария, слова молитвы и адлибы\nУчи нас принимать, что не изменить\nВручи шанс поменять что-то, исцелить\nВ ночи среди нас натянута вести нить\nЧтоб вместе вспоминать ушедших лица, то, что есть — ценить\nЯ воспитал культуру, как сына\nУлицы — моё второе имя, хоть не Майк Скиннер\nДа ладно, я стебусь, бро\nНе будь таким серьёзным, серьёзных часто не уважают"))
    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val printingApp: HttpHandler = DebuggingFilters.PrintRequest().then(app(renderer,books))

    val server = printingApp.asServer(Undertow(9000)).start()
    println("Server started on http://localhost:" + server.port())
}
