package ru.ac.uniyar.domain.db.tables

import org.ktorm.schema.*

const val AUTHOR_NAME_MAX_LENGTH = 100
const val AUTHOR_LOGIN_MAX_LENGTH = 40

object AuthorTable : Table<Nothing>("AUTHOR") {
    val login = varchar("LOGIN").primaryKey()
    val name = varchar("NAME")
    val registrationDate = datetime("REGISTRATION_DATE")
    val roleName = varchar("ROLE_NAME")
    val birthDate = date("BIRTH_DATE")
}
