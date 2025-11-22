package az.pashabank.starter.domain.usecase.customer

import az.pashabank.starter.domain.base.BaseFlowUseCase
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.model.customer.Customer
import az.pashabank.starter.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

class ObserveCustomerUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: CustomerRepository
) : BaseFlowUseCase<Unit, Customer>(context, converter) {

    override fun createFlow(params: Unit): Flow<Customer> {
        return repository.observeCustomers()
            .filterNotNull()
            .filter { it.isNotEmpty() }
            .map { it.first() }
    }
}