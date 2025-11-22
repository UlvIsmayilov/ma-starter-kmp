package az.pashabank.starter.domain.usecase.error

import az.pashabank.starter.domain.base.BaseUseCase
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.repository.ErrorConverterRepository
import kotlin.coroutines.CoroutineContext

class GetErrorFromCodeUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: ErrorConverterRepository,
) : BaseUseCase<GetErrorFromCodeUseCase.Params, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: Params) {
        return repository.getError(params.code, params.identifier)
    }

    data class Params(val code: Int, val identifier: String)
}