package app.chintan.prasadam.data.mapper

import app.chintan.prasadam.data.local.entity.RecipeEntity
import app.chintan.prasadam.domain.model.Difficulty
import app.chintan.prasadam.domain.model.LocalizedList
import app.chintan.prasadam.domain.model.LocalizedText
import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

/**
 * Extension function to convert a Room [RecipeEntity] to a domain [Recipe].
 * List columns stored as JSON strings are deserialized here.
 */
fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = id,
    slug = slug,
    name = LocalizedText(en = nameEn, hi = nameHi, gu = nameGu),
    description = LocalizedText(en = descriptionEn, hi = descriptionHi, gu = descriptionGu),
    category = runCatching { RecipeCategory.valueOf(category) }.getOrDefault(RecipeCategory.SNACKS),
    tags = runCatching {
        json.decodeFromString<List<String>>(tags)
    }.getOrDefault(emptyList()),
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    difficulty = runCatching { Difficulty.valueOf(difficulty) }.getOrDefault(Difficulty.EASY),
    isFarali = isFarali,
    isFestivalSpecial = isFestivalSpecial,
    isPopular = isPopular,
    ingredients = LocalizedList(
        en = decodeList(ingredientsEn),
        hi = decodeList(ingredientsHi),
        gu = decodeList(ingredientsGu)
    ),
    instructions = LocalizedList(
        en = decodeList(instructionsEn),
        hi = decodeList(instructionsHi),
        gu = decodeList(instructionsGu)
    ),
    notes = if (notesEn != null || notesHi != null || notesGu != null) {
        LocalizedText(en = notesEn.orEmpty(), hi = notesHi.orEmpty(), gu = notesGu.orEmpty())
    } else null,
    imageUrl = imageUrl,
    isFavorite = isFavorite
)

private fun decodeList(raw: String): List<String> =
    runCatching { json.decodeFromString<List<String>>(raw) }.getOrDefault(emptyList())
