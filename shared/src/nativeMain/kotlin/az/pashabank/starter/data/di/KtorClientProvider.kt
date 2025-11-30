package az.pashabank.starter.data.di

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun <T : HttpClientEngineConfig> platformClient(): HttpClientEngineFactory<T> {
    return Darwin as HttpClientEngineFactory<T>
}
