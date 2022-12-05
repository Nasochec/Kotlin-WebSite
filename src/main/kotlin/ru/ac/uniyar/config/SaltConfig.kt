package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.string

class SaltConfig(val salt: String) {
    companion object {
        val saltLens = EnvironmentKey.string().required("salt", "salt")
        val defaultEnv = Environment.defaults(saltLens of "defaultSaltIsVeryBad,PleaseUseEnvironmentToSetThis")
        fun fromEnvironment(environment: Environment) = SaltConfig(saltLens(environment))
    }
}
