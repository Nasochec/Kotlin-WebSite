package ru.ac.uniyar.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

const val DEFAULT_TOKEN_LIFE = 7
const val ACCEPT_LIFE_IN_SECONDS = 60L

class JwtTools(
    salt: String,
    val organizationName: String,
    val lifeTme: Period = Period.ofDays(DEFAULT_TOKEN_LIFE)
) {

    private val algorithm = Algorithm.HMAC512(salt)

    private val verifier = JWT
        .require(algorithm)
        .acceptExpiresAt(ACCEPT_LIFE_IN_SECONDS)
        .build()

    fun createToken(subject: String): String? {
        return try {
            JWT
                .create()
                .withSubject(subject)
                .withIssuer(organizationName)
                .withExpiresAt(
                    LocalDate
                        .now()
                        .plus(lifeTme)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                )
                .sign(algorithm)
        } catch (_: JWTCreationException) {
            return null
        }
    }

    fun subject(token: String): String? {
        return try {
            val decodedToken = verifier.verify(token)
            decodedToken.subject
        } catch (_: JWTCreationException) {
            return null
        }
    }
}
