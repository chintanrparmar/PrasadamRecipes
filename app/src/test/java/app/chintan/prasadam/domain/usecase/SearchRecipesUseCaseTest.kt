package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.model.Difficulty
import app.chintan.prasadam.domain.model.LocalizedList
import app.chintan.prasadam.domain.model.LocalizedText
import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory
import app.chintan.prasadam.domain.repository.RecipeRepository
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchRecipesUseCaseTest {

    private lateinit var repository: RecipeRepository
    private lateinit var useCase: SearchRecipesUseCase

    private val sabudana = recipe(
        id = 1,
        nameEn = "Sabudana Khichdi",
        nameHi = "साबूदाना खिचड़ी",
        tags = listOf("fasting", "farali"),
        ingredientsEn = listOf("sabudana", "peanuts", "potatoes")
    )

    private val thepla = recipe(
        id = 2,
        nameEn = "Methi Thepla",
        nameHi = "मेथी थेपला",
        tags = listOf("flatbread", "gujarati"),
        ingredientsEn = listOf("wheat flour", "fenugreek leaves", "yogurt")
    )

    private val kadhi = recipe(
        id = 3,
        nameEn = "Gujarati Kadhi",
        nameHi = "गुजराती कढ़ी",
        tags = listOf("soup", "quick"),
        ingredientsEn = listOf("yogurt", "gram flour", "ginger")
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchRecipesUseCase(repository)
    }

    @Test
    fun `blank query returns all recipes`() = runTest {
        every { repository.getAllRecipes() } returns flowOf(listOf(sabudana, thepla, kadhi))
        every { repository.searchRecipes(any()) } returns flowOf(emptyList())

        useCase("").test {
            assertEquals(3, awaitItem().size)
            awaitComplete()
        }
    }

    @Test
    fun `query matches recipe name in English`() = runTest {
        every { repository.searchRecipes("thepla") } returns flowOf(listOf(thepla))

        useCase("thepla").test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Methi Thepla", result.first().name.en)
            awaitComplete()
        }
    }

    @Test
    fun `query matches recipe by ingredient`() = runTest {
        every { repository.searchRecipes("yogurt") } returns flowOf(listOf(thepla, kadhi))

        useCase("yogurt").test {
            val result = awaitItem()
            assertEquals(2, result.size)
            awaitComplete()
        }
    }

    @Test
    fun `query matches recipe by tag`() = runTest {
        every { repository.searchRecipes("fasting") } returns flowOf(listOf(sabudana))

        useCase("fasting").test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Sabudana Khichdi", result.first().name.en)
            awaitComplete()
        }
    }

    @Test
    fun `query with no match returns empty list`() = runTest {
        every { repository.searchRecipes("pizza") } returns flowOf(emptyList())

        useCase("pizza").test {
            assertTrue(awaitItem().isEmpty())
            awaitComplete()
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun recipe(
        id: Int,
        nameEn: String,
        nameHi: String = "",
        tags: List<String> = emptyList(),
        ingredientsEn: List<String> = emptyList()
    ) = Recipe(
        id = id,
        slug = nameEn.lowercase().replace(" ", "_"),
        name = LocalizedText(en = nameEn, hi = nameHi, gu = ""),
        description = LocalizedText(en = "", hi = "", gu = ""),
        category = RecipeCategory.SNACKS,
        tags = tags,
        prepTimeMinutes = 10,
        cookTimeMinutes = 20,
        servings = 2,
        difficulty = Difficulty.EASY,
        isFarali = false,
        isFestivalSpecial = false,
        isPopular = false,
        ingredients = LocalizedList(en = ingredientsEn, hi = emptyList(), gu = emptyList()),
        instructions = LocalizedList(en = emptyList(), hi = emptyList(), gu = emptyList()),
        notes = null,
        imageUrl = null
    )
}
