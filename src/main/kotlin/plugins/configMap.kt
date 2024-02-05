package plugins

import io.ktor.server.config.*


fun ApplicationConfig.toMap(path: String): Map<String, Any?> {
    return config(path).keys().associateBy({ it }, { config(path).property(it).getString() })
}