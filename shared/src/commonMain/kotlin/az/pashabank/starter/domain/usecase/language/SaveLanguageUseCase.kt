package az.pashabank.starter.domain.usecase.language

import az.pashabank.starter.domain.base.BaseUseCase
import az.pashabank.starter.domain.constant.AppLanguage
import az.pashabank.starter.domain.exceptions.ErrorConverter
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import kotlin.coroutines.CoroutineContext

class SaveLanguageUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val dataSource: AppSettingsDataSource
) : BaseUseCase<AppLanguage, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: AppLanguage) {
        dataSource.setAppLanguage(params)
    }
}