package app.chintan.prasadam.feature.settings

import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.ThemePreference
import app.chintan.prasadam.domain.model.UserPreferences

data class SettingsUiState(
    val isLoading: Boolean = true,
    val preferences: UserPreferences = UserPreferences()
) {
    val selectedLanguage: Language get() = preferences.language
    val selectedTheme: ThemePreference get() = preferences.themePreference
}
