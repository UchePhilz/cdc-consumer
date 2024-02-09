package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 */
@Serializable
@SerialName("schema")
data class Schema<T>(@SerialName("payload") @Contextual val payload: Payload<T>)


/**
 *
 */
@Serializable
@SerialName("payload")
data class Payload<T>(
    @SerialName("after") @Contextual val data: T,
                 @SerialName("op") val operationType: Char)

/**
 *
 */
@Serializable
data class Car(
    @SerialName("car_manufacturer") val carManufacturer: String,
    @SerialName("car_brand") val carBrand: String,
    @SerialName("car_model") val carModel: String,
)
