package plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import models.Payload
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import service.ServiceInterface
import java.time.Duration
import java.util.*

/**
 * Launches a Kafka consumer for the specified topic, continuously polls messages,
 * and processes them using the provided service interface.
 *
 * @param app The Ktor Application instance.
 * @param json The JSON parser instance used to deserialize JSON messages.
 * @param topic The Kafka topic from which to consume messages.
 * @param service The service interface defining methods for processing Kafka messages.
 * @param <T> The type of data processed by the service.
 */
fun <T> launchConsumer(
    app: Application,
    config: ApplicationConfig,
    json: Json,
    topic: String,
    service: ServiceInterface<T>,
) {
    val consumer = createKafkaConsumer<String, String>(config, topic)
    app.launch {
        try {
            while (true) {
                poll(consumer, json, service)
            }
        } finally {
            consumer.apply {
                unsubscribe()
            }
            app.log.info("consumer for ${consumer.groupMetadata().groupId()} unsubscribed and closed...")
        }
    }
}

/**
 * private suspend function asynchronously polls messages from a Kafka consumer
 * and processes them using the provided service interface.
 *
 * @param consumer: The Kafka consumer instance responsible for polling messages.
 * @param json: The JSON parser instance used to deserialize JSON messages.
 * @param serviceInterface: The service interface defining methods for processing Kafka messages.
 *
 * @param T: The type of data processed by the service.
 *
 */
private suspend fun <T> poll(
    consumer: KafkaConsumer<String, String>,
    json: Json,
    serviceInterface: ServiceInterface<T>,
) {
    withContext(Dispatchers.IO) {

        consumer.poll(Duration.ofMillis(100))
            .forEach {

                printConsoleBorder()

                val jsonString = it.value()

                whenNotNull(jsonString) { msg ->

                    //println("Json is ${it.key()} - ${msg}")

                    val payload = serviceInterface.getObject(jsonString = jsonString, json = json)

                    println("group id: ${consumer.groupMetadata().groupId()}")
                    println(payload)

                    executeService(payload.payload, serviceInterface)

                }
                printConsoleBorder()
            }
    }
}

/**
 * private function executes a service operation based on the provided payload and service interface.
 *
 * @param payload: The payload containing data for the service operation.
 * @param serviceInterface: The service interface defining methods for create, update, and delete operations.
 *
 * @param T: The type of data operated on by the service.
 *
 */
private fun <T> executeService(payload: Payload<T>, serviceInterface: ServiceInterface<T>) {
    when (payload.operationType) {
        'c' -> serviceInterface.create(payload.afterData)
        'u' -> serviceInterface.update(payload.afterData)
        'd' -> serviceInterface.delete(payload.beforeData)
        else -> {
            println("Operation not allowed")
        }
    }
}


/**
 * Build KafkaConsumer
 *
 * buildConsumer creates and configure a Kafka consumer
 * instance with the configuration file.
 *
 * @param config: ApplicationConfig - represents the configuration object
 * used to retrieve Kafka-related properties.
 *
 * @return KafkaConsumer
 */
private fun <K, V> buildConsumer(
    config: ApplicationConfig,
): KafkaConsumer<K, V> {
    val bootstrapServers: List<String> = config.property("ktor.kafka.bootstrap.servers").getList()

    // common config
    val commonConfig = config.toMap("ktor.kafka.properties")

    // get consumer config
    val consumerConfig = config.toMap("ktor.kafka.consumer")

    val consumerProperties: Properties = Properties().apply {
        putAll(commonConfig)
        putAll(consumerConfig)
        //put("group.id", groupId)
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    }
    return KafkaConsumer(consumerProperties)
}

/**
 * Create a kafka consumer for a topic
 *
 * Create Kafka consumer instance for a specific topic using the provided configuration.
 *
 * @param config: ApplicationConfig - Configuration object used to retrieve Kafka-related properties.
 * @param topic: String - Parameter topic of type String, representing the Kafka topic to subscribe to.
 *
 * @return KafkaConsumer
 */
fun <K, V> createKafkaConsumer(
    config: ApplicationConfig,
    topic: String,
): KafkaConsumer<K, V> {
    val consumer = buildConsumer<K, V>(config)
    consumer.subscribe(listOf(topic))
    return consumer
}


private fun printConsoleBorder() {
    println("|||||||||||||||||||||||")
    println("-----------------------")
}


/**
 *  Extension function converts properties specified by a given path in the ApplicationConfig to a Map<String, Any?>.
 *
 * @param path: The path specifying the properties in the ApplicationConfig to be converted to a map.
 *
 * @return Returns a Map<String, Any?> where keys are property names and values are their corresponding values.
 */
fun ApplicationConfig.toMap(path: String): Map<String, Any?> {
    return config(path).keys().associateBy({ it }, { config(path).property(it).getString() })
}

/**
 * This function allows executing a callback if the input value is not null.<br>
 * It checks if the input value is not null, and if so, invokes the provided callback with the non-null value.
 *
 * @param T: The type of the input value.
 * @param R: The return type of the callback function.
 *
 * @param input: The input value to be checked for null.
 * @param callback: The callback function to be executed if the input value is not null.
 *
 * @return Returns the result of the callback function if the input value is not null.
 */
inline fun <T : Any, R> whenNotNull(input: T?, callback: (T) -> R): R? {
    return input?.let(callback)
}