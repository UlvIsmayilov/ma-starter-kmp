package az.pashabank.starter.domain.repository

import az.pashabank.starter.domain.model.customer.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun observeCards(): Flow<List<Card>>
    suspend fun syncCards()
}