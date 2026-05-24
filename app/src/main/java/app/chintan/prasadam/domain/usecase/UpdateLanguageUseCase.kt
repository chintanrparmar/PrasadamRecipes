package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Persists the user's selected [Language] preference.
 */
class UpdateLanguageUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(language: Language) = repository.updateLanguage(language)
}
