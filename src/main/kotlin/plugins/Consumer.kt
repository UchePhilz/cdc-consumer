package plugins

import io.ktor.server.config.*
import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.*

/**
 * Build KafkaConsumer
 *
 * BuildConsumer creates and configure a Kafka consumer
 * instance with the configuration file.
 *
 * @param config: ApplicationConfig - represents the configuration object
 * used to retrieve Kafka-related properties.
 * @param groupId: String - Optional parameter groupId of type String with
 * a default value of "ktor-consumer".
 *
 * @return KafkaConsumer
 */
fun <K, V> buildConsumer(config: ApplicationConfig,
                         groupId: String = "ktor-consumer"): KafkaConsumer<K, V> {
    val bootstrapServers: List<String> = config.property("ktor.kafka.bootstrap.servers").getList()

    // common config
    val commonConfig = config.toMap("ktor.kafka.properties")

    // get consumer config
    val consumerConfig = config.toMap("ktor.kafka.consumer")

    val consumerProperties: Properties = Properties().apply {
        putAll(commonConfig)
        putAll(consumerConfig)
        put("group.id", groupId)
        put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
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
 * @param groupId: String - Optional parameter groupId of type String with a default value of "ktor-consumer".
 *
 * @return KafkaConsumer
 */
fun <K, V> createKafkaConsumer(
    config: ApplicationConfig, topic: String, groupId: String = "ktor-consumer"
): KafkaConsumer<K, V> {
    val consumer = buildConsumer<K, V>(config, groupId)
    consumer.subscribe(listOf(topic))
    return consumer
}
