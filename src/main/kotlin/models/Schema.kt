package models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("schema")
class Schema<T>(@SerialName("payload") @Contextual val payload: Payload<T>)

@Serializable
@SerialName("payload")
class Payload<T>(@SerialName("after") @Contextual val data: T)

@Serializable
class LgConnectivity(
    @SerialName("comment") val message: String,
    @SerialName("consulted_with") val consultedWith: String,
    @SerialName("function_at_thermondo") val functionAtThermondo: String,
    @SerialName("other_comments") val hasMoreComments: Boolean,
    @SerialName("othe_coments") val unrealCol: Boolean?
)