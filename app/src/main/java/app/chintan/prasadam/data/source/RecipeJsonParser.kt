package app.chintan.prasadam.data.source

import android.content.Context
import app.chintan.prasadam.data.local.entity.RecipeEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

// ── JSON DTOs ─────────────────────────────────────────────────────────────────

@Serializable
private data class LocalizedTextDto(
    val en: String = "",
    val hi: String = "",
    val gu: String = ""
)

@Serializable
private data class LocalizedListDto(
    val en: List<String> = emptyList(),
    val hi: List<String> = emptyList(),
    val gu: List<String> = emptyList()
)

@Serializable
private data class RecipeDto(
    val id: Int,
    val slug: String,
    val name: LocalizedTextDto,
    val description: LocalizedTextDto,
    val category: String,
    val tags: List<String> = emptyList(),
    @SerialName("prepTimeMinutes") val prepTimeMinutes: Int = 0,
    @SerialName("cookTimeMinutes") val cookTimeMinutes: Int = 0,
    val servings: Int = 2,
    val difficulty: String = "EASY",
    val isFarali: Boolean = false,
    val isFestivalSpecial: Boolean = false,
    val isPopular: Boolean = false,
    val ingredients: LocalizedListDto = LocalizedListDto(),
    val instructions: LocalizedListDto = LocalizedListDto(),
    val notes: LocalizedTextDto? = null,
    val imageUrl: String? = null
)

@Serializable
private data class RecipeListDto(val recipes: List<RecipeDto>)

// ── Parser ────────────────────────────────────────────────────────────────────

/**
 * Reads [recipes.json] from the app assets folder, deserialises it, and
 * converts every entry into a [RecipeEntity] ready for Room insertion.
 *
 * List fields are re-encoded as JSON strings because Room stores them as
 * TEXT columns (see [app.chintan.prasadam.data.local.database.Converters]).
 */
@Singleton
class RecipeJsonParser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun parseRecipes(): List<RecipeEntity> {
        val raw = context.assets.open("recipes.json").bufferedReader().readText()
        val dto = json.decodeFromString<RecipeListDto>(raw)
        return dto.recipes.map { it.toEntity() }
    }

    private fun List<String>.toJsonString(): String = json.encodeToString(this)

    private fun RecipeDto.toEntity() = RecipeEntity(
        id = id,
        slug = slug,
        nameEn = name.en,
        nameHi = name.hi,
        nameGu = name.gu,
        descriptionEn = description.en,
        descriptionHi = description.hi,
        descriptionGu = description.gu,
        category = category,
        tags = tags.toJsonString(),
        prepTimeMinutes = prepTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        servings = servings,
        difficulty = difficulty,
        isFarali = isFarali,
        isFestivalSpecial = isFestivalSpecial,
        isPopular = isPopular,
        ingredientsEn = ingredients.en.toJsonString(),
        ingredientsHi = ingredients.hi.toJsonString(),
        ingredientsGu = ingredients.gu.toJsonString(),
        instructionsEn = instructions.en.toJsonString(),
        instructionsHi = instructions.hi.toJsonString(),
        instructionsGu = instructions.gu.toJsonString(),
        notesEn = notes?.en,
        notesHi = notes?.hi,
        notesGu = notes?.gu,
        imageUrl = imageUrl,
        isFavorite = false
    )
}
