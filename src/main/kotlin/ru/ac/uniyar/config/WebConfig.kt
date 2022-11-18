package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int

class WebConfig(val webPort: Int) {

    companion object {
        val webPortLens = EnvironmentKey.int().required("web.port", "Application web port")
        val defaultEnv = Environment.defaults(
            webPortLens of 1515,
        )
        fun fromEnvironment(environment: Environment): WebConfig = WebConfig(webPortLens(environment))
    }
}
