package ru.ac.uniyar.web.lens

import org.http4k.lens.Lens
import org.http4k.lens.LensFailure

fun <IN : Any, OUT>lensOrNull(lens: Lens<IN, OUT?>, value: IN): OUT? =
    try {
        lens.invoke(value)
    } catch (_: LensFailure) {
        null
    }

