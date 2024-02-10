package plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.serialization.json.Json
import service.CarDriverDriverService
import service.CarService

private val config = ApplicationConfig("kafka.conf")

fun Application.carConsumer() {

    launchConsumer(
        app = this,
        config = config,
        json = json,
        topic = "source.public.car",
        service = CarService()
    )
}

fun Application.carDriverConsumer() {

    launchConsumer(
        app = this,
        config = config,
        json = json,
        topic = "source.public.car_driver",
        service = CarDriverDriverService()
    )
}

val json = Json {
    ignoreUnknownKeys = true // ignore fields in json that are not in object without throwing error
    explicitNulls = false // in case field doesn't exist, return null
}
