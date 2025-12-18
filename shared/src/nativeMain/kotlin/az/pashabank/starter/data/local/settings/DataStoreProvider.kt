package az.pashabank.starter.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun Scope.createDataStore(): DataStore<Preferences> {
    return createDataStore(
        produceFile = {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory).path + "/app_settings.preferences_pb"
        }
    )
}

private fun createDataStore(produceFile: () -> String): DataStore<Preferences> {
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = { produceFile() }
    )
}
