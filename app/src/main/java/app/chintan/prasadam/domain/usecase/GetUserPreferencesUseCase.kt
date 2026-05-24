package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.UserPreferences
import app.chintan.prasadam.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Exposes the live [UserPreferences] stream for the current user.
 */
class GetUserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserPreferences> = repository.userPreferences
}
