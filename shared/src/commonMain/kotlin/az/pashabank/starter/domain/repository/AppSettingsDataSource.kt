package az.pashabank.starter.domain.repository

import az.pashabank.starter.domain.constant.AppLanguage
import kotlinx.coroutines.flow.Flow

interface AppSettingsDataSource {
    suspend fun getAppLanguage(): AppLanguage
    suspend fun setAppLanguage(langCode: AppLanguage)
    fun observeLanguage(): Flow<AppLanguage>
}