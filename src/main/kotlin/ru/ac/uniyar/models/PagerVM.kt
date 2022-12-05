package ru.ac.uniyar.models

import org.http4k.template.ViewModel

sealed class PagerVM(val pager: Pager) : ViewModel
