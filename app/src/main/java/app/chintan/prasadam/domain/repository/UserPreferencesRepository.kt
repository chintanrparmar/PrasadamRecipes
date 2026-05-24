package app.chintan.prasadam.domain.repository

import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.ThemePreference
import app.chintan.prasadam.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Contract for reading and writing user preferences backed by DataStore.
 */
interface UserPreferencesRepository {
    /** Emits the latest [UserPreferences] whenever they change. */
    val userPreferences: Flow<UserPreferences>

    /** Persists the selected [language]. */
    suspend fun updateLanguage(language: Language)

    /** Persists the selected [themePreference]. */
    suspend fun updateTheme(themePreference: ThemePreference)
}
