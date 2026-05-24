package app.chintan.prasadam.domain.model

/**
 * Core domain model for a Prasadam recipe.
 * All text fields are localized via [LocalizedText] or [LocalizedList].
 *
 * This model is immutable and platform-agnostic — it belongs purely to the
 * domain layer and should not import Android or data-layer types.
 */
data class Recipe(
    val id: Int,
    val slug: String,
    val name: LocalizedText,
    val description: LocalizedText,
    val category: RecipeCategory,
    val tags: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: Difficulty,
    val isFarali: Boolean,
    val isFestivalSpecial: Boolean,
    val isPopular: Boolean,
    val ingredients: LocalizedList,
    val instructions: LocalizedList,
    val notes: LocalizedText?,
    val imageUrl: String?,
    val isFavorite: Boolean = false
) {
    /** Total time in minutes (prep + cook). */
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes

    /** True if total time is under 30 minutes (cook-time-only for practical purposes). */
    val isQuick: Boolean get() = cookTimeMinutes <= 30
}
