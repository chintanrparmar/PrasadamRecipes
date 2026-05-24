package app.chintan.prasadam.domain.model

/**
 * User-configurable preferences persisted via DataStore.
 */
data class UserPreferences(
    val language: Language = Language.ENGLISH,
    val themePreference: ThemePreference = ThemePreference.SYSTEM
)
