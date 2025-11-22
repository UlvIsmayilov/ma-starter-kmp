package az.pashabank.starter.domain.usecase.card

import az.pashabank.starter.domain.base.BaseFlowUseCase
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.model.customer.Card
import az.pashabank.starter.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class ObserveCardsUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: CardRepository
) : BaseFlowUseCase<Unit, List<Card>>(context, converter) {

    override fun createFlow(params: Unit): Flow<List<Card>> {
        return repository.observeCards()
    }

}