package az.pashabank.starter.data.remote

import az.pashabank.starter.data.remote.model.customer.CustomerRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CustomerApiService(private val httpClient: HttpClient) {
    suspend fun getCustomer(customerId: String): CustomerRemoteDto {
        return httpClient.get("pb/v1/customers/$customerId").body()
    }
}
