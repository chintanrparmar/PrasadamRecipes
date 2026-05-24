package app.chintan.prasadam.domain.repository

import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory
import kotlinx.coroutines.flow.Flow

/**
 * Contract for accessing recipe data.
 * Room is the single source of truth after first-launch seeding.
 */
interface RecipeRepository {
    /** Emits the full recipe list whenever the database changes. */
    fun getAllRecipes(): Flow<List<Recipe>>

    /** Emits recipes filtered by [category]. Returns all recipes when category is ALL. */
    fun getRecipesByCategory(category: RecipeCategory): Flow<List<Recipe>>

    /** Returns a single recipe by its [id], or null if not found. */
    fun getRecipeById(id: Int): Flow<Recipe?>

    /** Emits only recipes marked as favorite. */
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    /** Emits recipes matching the [query] against name, tags, and ingredients. */
    fun searchRecipes(query: String): Flow<List<Recipe>>

    /** Toggles the favorite state of the recipe with [recipeId]. */
    suspend fun toggleFavorite(recipeId: Int)

    /** Returns true if the local database has already been seeded from JSON. */
    suspend fun isDataSeeded(): Boolean

    /** Seeds the Room database from the bundled JSON asset. */
    suspend fun seedFromJson()
}
