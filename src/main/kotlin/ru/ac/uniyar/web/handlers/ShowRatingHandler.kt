package ru.ac.uniyar.web.handlers

// fun showEditRating(
//    htmlView: ContextAwareViewRender,
//    getRating: GetRating
// ): HttpHandler = { request ->
//    neededAgePathLens(request)?.let { neededAge ->
//        getRating.get(neededAge)
//    }?.let { rating ->
//        Response(Status.OK).with(htmlView(request) of AddRatingVM(rating = rating, isEdit = true))
//    } ?: Response(Status.BAD_REQUEST)
// }
//
// fun editRating(
//    htmlView: ContextAwareViewRender,
//    getRating: GetRating,
//    editRating: EditRating
// ): HttpHandler = { request ->
//    val oldNeededAge = neededAgePathLens(request)
//
//
// }
