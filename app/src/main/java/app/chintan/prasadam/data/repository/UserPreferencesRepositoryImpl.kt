package app.chintan.prasadam.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.ThemePreference
import app.chintan.prasadam.domain.model.UserPreferences
import app.chintan.prasadam.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val THEME = stringPreferencesKey("theme")
    }

    override val userPreferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        val langCode = prefs[Keys.LANGUAGE] ?: Language.ENGLISH.name
        val themeCode = prefs[Keys.THEME] ?: ThemePreference.SYSTEM.name
        UserPreferences(
            language = runCatching { Language.valueOf(langCode) }.getOrDefault(Language.ENGLISH),
            themePreference = runCatching { ThemePreference.valueOf(themeCode) }.getOrDefault(ThemePreference.SYSTEM)
        )
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    override suspend fun updateTheme(themePreference: ThemePreference) {
        dataStore.edit { it[Keys.THEME] = themePreference.name }
    }
}
