package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.repository.RecipeRepository
import javax.inject.Inject

/**
 * Toggles the favorite state of a recipe.
 * If the recipe is currently a favorite it will be un-favorited, and vice-versa.
 */
class ToggleFavoriteRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: Int) = repository.toggleFavorite(recipeId)
}
