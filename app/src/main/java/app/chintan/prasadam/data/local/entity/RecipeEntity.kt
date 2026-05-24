package app.chintan.prasadam.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity that mirrors the recipe JSON schema.
 * Localized fields are stored as flat columns for query performance.
 * List fields (ingredients, instructions, tags) are stored as JSON strings
 * and converted via [app.chintan.prasadam.data.local.database.Converters].
 */
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val slug: String,

    // Localized name
    val nameEn: String,
    val nameHi: String,
    val nameGu: String,

    // Localized description
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionGu: String,

    // Category as string (enum name)
    val category: String,

    // Tags as JSON string (e.g. ["fasting","farali"])
    val tags: String,

    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,

    // Difficulty as string (enum name)
    val difficulty: String,

    val isFarali: Boolean,
    val isFestivalSpecial: Boolean,
    val isPopular: Boolean,

    // Ingredients — one JSON array per language
    val ingredientsEn: String,
    val ingredientsHi: String,
    val ingredientsGu: String,

    // Instructions — one JSON array per language
    val instructionsEn: String,
    val instructionsHi: String,
    val instructionsGu: String,

    // Optional notes
    val notesEn: String? = null,
    val notesHi: String? = null,
    val notesGu: String? = null,

    val imageUrl: String? = null,

    /** Toggled by the user; not present in the source JSON. */
    val isFavorite: Boolean = false
)
