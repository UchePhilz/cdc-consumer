import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import models.Car
import models.Schema
import org.apache.kafka.clients.consumer.KafkaConsumer
import plugins.createKafkaConsumer
import java.time.Duration

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    val config = ApplicationConfig("kafka.conf")
    val consumer: KafkaConsumer<String, String> =
        createKafkaConsumer(config,"source.public.car")
    launch {
        try {
            while (true) {
                pollCar(consumer)
            }
        } finally {
            consumer.apply {
                unsubscribe()
            }
            log.info("consumer for ${consumer.groupMetadata().groupId()} unsubscribed and closed...")
        }
    }

}

val json = Json {
    ignoreUnknownKeys = true // ignore fields in json that are not in object without throwing error
    explicitNulls = false // in case field doesn't exist, return null
}

private suspend fun pollCar(consumer: KafkaConsumer<String, String>) =
    withContext(Dispatchers.IO) {
        consumer.poll(Duration.ofMillis(100))
            .forEach {

                println("-----------------------")
                println("-----------------------")

                val decodeFromString = json.decodeFromString<Schema<Car>>(it.value())
                println(decodeFromString.payload.data)

                println("-----------------------")
                println("-----------------------")
            }
    }