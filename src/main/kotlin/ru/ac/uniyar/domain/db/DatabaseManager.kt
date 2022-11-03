package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect

/**Константа для задания размера страницы, чтобы это можно было поменять в любой момент.**/
const val PageLenght: Int = 10

fun connectToDatabase() = Database.connect(
    url = H2DatabaseManager.JDBC_CONNECTION,
    driver = "org.h2.Driver",
    user = "sa",
    dialect = MySqlDialect(),
)
