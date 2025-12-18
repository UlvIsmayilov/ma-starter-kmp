package az.pashabank.starter.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.scope.Scope

expect fun Scope.createDataStore(): DataStore<Preferences>
