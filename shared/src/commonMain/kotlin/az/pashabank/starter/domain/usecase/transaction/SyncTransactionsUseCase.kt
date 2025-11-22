package az.pashabank.starter.domain.usecase.transaction

import az.pashabank.starter.domain.base.BaseUseCase
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.repository.TransactionRepository
import kotlin.coroutines.CoroutineContext

class SyncTransactionsUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: TransactionRepository
) : BaseUseCase<SyncTransactionsUseCase.Param, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: Param) {
        repository.syncTransactions(params.cardId)
    }

    data class Param(val cardId: String)
}