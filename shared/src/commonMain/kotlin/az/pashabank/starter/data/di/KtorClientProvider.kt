package az.pashabank.starter.data.di

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect fun <T : HttpClientEngineConfig>  platformClient(): HttpClientEngineFactory<T>