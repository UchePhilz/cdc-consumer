package service

import kotlinx.serialization.json.Json
import models.Car
import models.Schema

class CarService : ServiceInterface<Car> {
    override fun create(t: Car?): Boolean {
        println("create car $t")
        return true
    }

    override fun update(t: Car?): Boolean {
        println("update car $t")
        return true
    }

    override fun delete(t: Car?): Boolean {
        println("delete car $t")
        return true
    }

    override fun getObject(jsonString: String, json: Json): Schema<Car> {
        return json.decodeFromString<Schema<Car>>(jsonString)
    }

}