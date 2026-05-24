package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.ThemePreference
import app.chintan.prasadam.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Persists the user's selected [ThemePreference].
 */
class UpdateThemeUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(themePreference: ThemePreference) =
        repository.updateTheme(themePreference)
}
