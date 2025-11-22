package az.pashabank.starter.domain.usecase.customer

import az.pashabank.starter.domain.base.BaseUseCase
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.repository.CustomerRepository
import kotlin.coroutines.CoroutineContext

class SyncCustomersUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: CustomerRepository
) : BaseUseCase<Unit, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: Unit) {
        repository.syncCustomers()
    }
}