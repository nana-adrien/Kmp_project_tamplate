package empire.digiprem.kmptemplate.core.data.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import okio.Path.Companion.toPath

internal const val DATA_STORE_FILE_NAME = "app_prefs.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
