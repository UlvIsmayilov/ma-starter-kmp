package az.pashabank.starter.domain.repository

import az.pashabank.starter.domain.model.customer.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun observeCustomers(): Flow<List<Customer>>
    suspend fun syncCustomers()
}