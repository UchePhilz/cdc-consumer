package service

import kotlinx.serialization.json.Json
import models.CarDriver
import models.Schema

class CarDriverDriverService : ServiceInterface<CarDriver> {
    override fun create(t: CarDriver?): Boolean {
        println("create CarDriver $t")
        return true
    }

    override fun update(t: CarDriver?): Boolean {
        println("update CarDriver $t")
        return true
    }

    override fun delete(t: CarDriver?): Boolean {
        println("delete CarDriver $t")
        return true
    }

    override fun getObject(jsonString: String, json: Json): Schema<CarDriver> {
        return json.decodeFromString<Schema<CarDriver>>(jsonString)
    }

}