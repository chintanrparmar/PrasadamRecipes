package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns the live list of recipes the user has marked as favorite.
 */
class GetFavoriteRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> = repository.getFavoriteRecipes()
}
