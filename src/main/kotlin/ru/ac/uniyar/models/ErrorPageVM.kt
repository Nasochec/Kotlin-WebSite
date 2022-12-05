package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.template.ViewModel

class ErrorPageVM(
    val errorUri: Uri,
    val message: String? = null
) : ViewModel
