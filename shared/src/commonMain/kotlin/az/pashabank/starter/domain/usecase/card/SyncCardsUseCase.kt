package az.pashabank.starter.domain.usecase.card

import az.pashabank.starter.domain.base.BaseUseCase
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.repository.CardRepository
import kotlin.coroutines.CoroutineContext

class SyncCardsUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: CardRepository
) : BaseUseCase<Unit, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: Unit) {
        repository.syncCards()
    }
}