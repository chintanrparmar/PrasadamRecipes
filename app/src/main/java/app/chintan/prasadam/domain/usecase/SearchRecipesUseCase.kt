package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Searches recipes by name, tags, or ingredients.
 *
 * - An empty or blank [query] returns all recipes (matches everything).
 * - Matching is case-insensitive and trims surrounding whitespace.
 *
 * Room handles the SQL-level search; this use case adds in-memory refinement
 * for ingredient matching across all three languages.
 */
class SearchRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(query: String): Flow<List<Recipe>> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return repository.getAllRecipes()

        return repository.searchRecipes(trimmed).map { recipes ->
            // Secondary in-memory pass: match ingredients in all languages
            val lower = trimmed.lowercase()
            recipes.filter { recipe ->
                val ingredientsMatch = recipe.ingredients.en.any { it.lowercase().contains(lower) } ||
                    recipe.ingredients.hi.any { it.lowercase().contains(lower) } ||
                    recipe.ingredients.gu.any { it.lowercase().contains(lower) }
                // Primary name / tag match is already handled by Room; include if either matches
                val tagMatch = recipe.tags.any { it.lowercase().contains(lower) }
                val nameMatch = recipe.name.en.lowercase().contains(lower) ||
                    recipe.name.hi.lowercase().contains(lower) ||
                    recipe.name.gu.lowercase().contains(lower)
                nameMatch || tagMatch || ingredientsMatch
            }
        }
    }
}
