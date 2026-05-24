package app.chintan.prasadam.data.source

import app.chintan.prasadam.domain.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeds the Room database from [recipes.json] on first app launch.
 *
 * This runs on [Dispatchers.IO] in a [SupervisorJob]-backed scope so it does not
 * block the main thread and does not cancel if a child coroutine fails.
 */
@Singleton
class DataSeedManager @Inject constructor(
    private val repository: RecipeRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun seedIfNeeded() {
        scope.launch {
            if (!repository.isDataSeeded()) {
                repository.seedFromJson()
            }
        }
    }
}
