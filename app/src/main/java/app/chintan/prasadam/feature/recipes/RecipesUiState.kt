package app.chintan.prasadam.feature.recipes

import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory

data class RecipesUiState(
    val isLoading: Boolean = true,
    val recipes: List<Recipe> = emptyList(),
    val selectedCategory: RecipeCategory = RecipeCategory.ALL,
    val searchQuery: String = "",
    val error: String? = null
) {
    val filteredRecipes: List<Recipe>
        get() = if (searchQuery.isBlank()) recipes else recipes  // filtering is handled upstream via use-case
}
