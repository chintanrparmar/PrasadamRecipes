package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns a single recipe by its [recipeId], or null if not found.
 */
class GetRecipeByIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: Int): Flow<Recipe?> = repository.getRecipeById(recipeId)
}
