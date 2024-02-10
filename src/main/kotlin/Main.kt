import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import plugins.carConsumer
import plugins.carDriverConsumer

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    carConsumer()
    carDriverConsumer()

}





