package service

import kotlinx.serialization.json.Json
import models.Schema

interface ServiceInterface<T> {

    fun create(t: T?): Boolean

    fun update(t: T?): Boolean

    fun delete(t: T?): Boolean

    /**
     * Support serialization for service
     */
    fun getObject(jsonString: String, json: Json): Schema<T>

}