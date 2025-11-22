package az.pashabank.starter.domain.usecase.language

import az.pashabank.starter.domain.base.BaseFlowUseCase
import az.pashabank.starter.domain.constant.AppLanguage
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class ObserveAppLanguageUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val dataSource: AppSettingsDataSource,
): BaseFlowUseCase<Unit, AppLanguage>(context, converter) {

    override fun createFlow(params: Unit): Flow<AppLanguage> = dataSource.observeLanguage()
}