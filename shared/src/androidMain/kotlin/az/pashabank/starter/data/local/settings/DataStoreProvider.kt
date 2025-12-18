package az.pashabank.starter.data.local.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.scope.Scope

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

actual fun Scope.createDataStore(): DataStore<Preferences> {
    val context = get<Context>().applicationContext
    return context.dataStore
}
