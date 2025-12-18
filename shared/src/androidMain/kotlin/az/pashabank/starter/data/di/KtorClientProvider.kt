@file:Suppress("UNCHECKED_CAST")

package az.pashabank.starter.data.di

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun <T : HttpClientEngineConfig> platformClient(): HttpClientEngineFactory<T> {
    return OkHttp as HttpClientEngineFactory<T>
}
