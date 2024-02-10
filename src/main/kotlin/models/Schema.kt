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
    /**
     * Will be null for delete operation
     */
    @SerialName("after") @Contextual val afterData: T?,
    /**
     * Will be null for create operation
     */
    @SerialName("before") @Contextual val beforeData: T?, // will be null for created
    /**
     * 'c' for inserts/create, 'u' for updates, 'd' for deletes
     */
    @SerialName("op") val operationType: Char, //
)


@Serializable
data class Car(
    @SerialName("car_manufacturer") val carManufacturer: String,
    @SerialName("car_brand") val carBrand: String,
    @SerialName("car_model") val carModel: String,
)

@Serializable
data class CarDriver(
    @SerialName("driver_name") val driverName: String,
    @SerialName("driver_license") val driverLicense: String,
    @SerialName("driver_age") val driverAge: Int,
)
