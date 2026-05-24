package app.chintan.prasadam.feature.home

import app.chintan.prasadam.domain.model.Recipe

data class HomeUiState(
    val isLoading: Boolean = true,
    val todayRecipe: Recipe? = null,
    val festivalRecipes: List<Recipe> = emptyList(),
    val quickRecipes: List<Recipe> = emptyList(),
    val popularRecipes: List<Recipe> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<Recipe> = emptyList(),
    val isSearchActive: Boolean = false,
    val error: String? = null
)
