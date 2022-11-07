package ru.ac.uniyar.domain.db

import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import ru.ac.uniyar.config.DatabaseConfig

/**Константа для задания размера страницы, чтобы это можно было поменять в любой момент.**/
const val PAGE_LENGTH: Int = 10

fun connectToDatabase() = Database.connect(
    url = H2DatabaseManager.JDBC_CONNECTION,
    driver = "org.h2.Driver",
    user = "sa",
    dialect = MySqlDialect(),
)

fun connectToDatabase(databaseConfig: DatabaseConfig) = Database.connect(
    url = databaseConfig.JDBCConnectionString,
    driver = "org.h2.Driver",
    user = "sa",
    dialect = MySqlDialect(),
)