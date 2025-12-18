package az.pashabank.starter.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import az.pashabank.starter.domain.constant.AppLanguage
import az.pashabank.starter.domain.constant.toAppLanguage
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppSettingsDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : AppSettingsDataSource {

    private val defaultLocale = "az"

    override suspend fun getAppLanguage(): AppLanguage {
        return dataStore.data.map { preferences ->
            val lang = preferences[LOCALE_KEY] ?: defaultLocale
            lang.toAppLanguage()
        }.first()
    }

    override suspend fun setAppLanguage(langCode: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LOCALE_KEY] = langCode.name
        }
    }

    override fun observeLanguage(): Flow<AppLanguage> {
        return dataStore.data.map { preferences ->
            val lang = preferences[LOCALE_KEY] ?: defaultLocale
            lang.toAppLanguage()
        }
    }

    companion object {
        private val LOCALE_KEY = stringPreferencesKey("locale")
    }
}
