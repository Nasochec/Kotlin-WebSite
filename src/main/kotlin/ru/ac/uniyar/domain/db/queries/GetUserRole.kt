package ru.ac.uniyar.domain.db.queries

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.authorization.Permissions
import ru.ac.uniyar.domain.db.tables.RoleTable

class GetUserRole(private val database: Database, private val getUserRoleName: GetUserRoleName) {
    fun get(login: String): Permissions =
        getUserRoleName.get(login)?.let { roleName ->
            database
                .from(RoleTable)
                .select()
                .where(RoleTable.name eq roleName)
                .mapNotNull(Permissions::fromQueryRowSet)
                .firstOrNull()
        } ?: Permissions("GUEST")
}
