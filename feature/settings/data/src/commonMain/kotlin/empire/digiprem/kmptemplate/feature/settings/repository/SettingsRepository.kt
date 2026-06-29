package empire.digiprem.kmptemplate.feature.settings.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import empire.digiprem.kmptemplate.feature.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
) : ISettingsRepository {

    private val KEY_DARK_THEME    = booleanPreferencesKey("dark_theme")
    private val KEY_LANGUAGE      = stringPreferencesKey("language")
    private val KEY_NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")

    override fun observeSettings(): Flow<AppSettings> =
        dataStore.data.map { prefs ->
            AppSettings(
                isDarkTheme          = prefs[KEY_DARK_THEME]    ?: false,
                language             = prefs[KEY_LANGUAGE]      ?: "en",
                notificationsEnabled = prefs[KEY_NOTIFICATIONS] ?: true,
            )
        }

    override suspend fun updateSettings(settings: AppSettings) {
        dataStore.edit { prefs ->
            prefs[KEY_DARK_THEME]    = settings.isDarkTheme
            prefs[KEY_LANGUAGE]      = settings.language
            prefs[KEY_NOTIFICATIONS] = settings.notificationsEnabled
        }
    }

    override suspend fun clearSettings() {
        dataStore.edit { it.clear() }
    }
}
