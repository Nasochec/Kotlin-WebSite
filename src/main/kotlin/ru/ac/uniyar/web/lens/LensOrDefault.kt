package ru.ac.uniyar.web.lens

import org.http4k.lens.Lens
import org.http4k.lens.LensFailure

fun <IN : Any, OUT> lensOrDefault(lens: Lens<IN, OUT?>, value: IN, default: OUT): OUT =
    try {
        lens.invoke(value) ?: default
    } catch (_: LensFailure) {
        default
    }
