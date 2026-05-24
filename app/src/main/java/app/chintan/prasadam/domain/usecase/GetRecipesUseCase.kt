package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory
import app.chintan.prasadam.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns all recipes, optionally filtered by [category].
 * When [category] is [RecipeCategory.ALL] or null, the full list is returned.
 */
class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(category: RecipeCategory? = null): Flow<List<Recipe>> =
        if (category == null || category == RecipeCategory.ALL) {
            repository.getAllRecipes()
        } else {
            repository.getRecipesByCategory(category)
        }
}
