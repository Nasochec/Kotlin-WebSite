package ru.ac.uniyar.web.lens

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.template.PebbleTemplates
import org.http4k.template.viewModel

val HTMLView = Body.viewModel(PebbleTemplates().HotReload("src/main/resources"), ContentType.TEXT_HTML).toLens()