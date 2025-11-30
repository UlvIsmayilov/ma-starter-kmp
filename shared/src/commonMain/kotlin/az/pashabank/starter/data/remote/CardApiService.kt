package az.pashabank.starter.data.remote

import az.pashabank.starter.data.remote.model.card.CardRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CardApiService(private val httpClient: HttpClient) {
    suspend fun getCards(customerId: String): List<CardRemoteDto> {
        return httpClient.get("pb/v1/customers/$customerId/cards").body()
    }
}
