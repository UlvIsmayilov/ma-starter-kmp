package az.pashabank.starter.data.di

import az.pashabank.starter.data.remote.CardApiService
import az.pashabank.starter.data.remote.CustomerApiService
import az.pashabank.starter.data.remote.TransactionApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteModule = module {

    single {
        Json {
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    single {
        HttpClient(platformClient()) {
            install(ContentNegotiation) {
                json(get<Json>())
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL // TODO: Make this configurable based on build type
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = getProperty("host")
                }
            }
        }
    }

    factory { CardApiService(get()) }
    factory { CustomerApiService(get()) }
    factory { TransactionApiService(get()) }
}
