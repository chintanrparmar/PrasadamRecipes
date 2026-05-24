package app.chintan.prasadam.feature.recipes

import app.chintan.prasadam.domain.model.Difficulty
import app.chintan.prasadam.domain.model.LocalizedList
import app.chintan.prasadam.domain.model.LocalizedText
import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory
import app.chintan.prasadam.domain.usecase.GetRecipesUseCase
import app.chintan.prasadam.domain.usecase.SearchRecipesUseCase
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getRecipesUseCase: GetRecipesUseCase
    private lateinit var searchRecipesUseCase: SearchRecipesUseCase
    private lateinit var viewModel: RecipesViewModel

    private val allRecipes = listOf(
        fakeRecipe(1, "Sabudana Khichdi", RecipeCategory.FARALI),
        fakeRecipe(2, "Methi Thepla", RecipeCategory.ROTI_BREAD),
        fakeRecipe(3, "Gujarati Kadhi", RecipeCategory.SOUP)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getRecipesUseCase = mockk()
        searchRecipesUseCase = mockk()
        every { getRecipesUseCase(null) } returns flowOf(allRecipes)
        every { getRecipesUseCase(RecipeCategory.ALL) } returns flowOf(allRecipes)
        every { searchRecipesUseCase(any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel = RecipesViewModel(getRecipesUseCase, searchRecipesUseCase)
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `after emission state has recipes`() = runTest {
        viewModel = RecipesViewModel(getRecipesUseCase, searchRecipesUseCase)

        viewModel.uiState.test {
            val initial = awaitItem()
            // Advance past StateFlow initial value
            if (initial.isLoading) {
                val loaded = awaitItem()
                assertFalse(loaded.isLoading)
                assertEquals(3, loaded.recipes.size)
            } else {
                assertFalse(initial.isLoading)
                assertEquals(3, initial.recipes.size)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `category selection resets search query`() = runTest {
        viewModel = RecipesViewModel(getRecipesUseCase, searchRecipesUseCase)
        every { getRecipesUseCase(RecipeCategory.FARALI) } returns flowOf(listOf(allRecipes[0]))

        viewModel.onCategorySelected(RecipeCategory.FARALI)

        viewModel.uiState.test {
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals("", viewModel.uiState.value.searchQuery)
        assertEquals(RecipeCategory.FARALI, viewModel.uiState.value.selectedCategory)
    }

    @Test
    fun `clearSearch resets query to blank`() = runTest {
        viewModel = RecipesViewModel(getRecipesUseCase, searchRecipesUseCase)
        every { searchRecipesUseCase("thepla") } returns flowOf(listOf(allRecipes[1]))

        viewModel.onSearchQueryChange("thepla")
        viewModel.clearSearch()

        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun fakeRecipe(id: Int, nameEn: String, category: RecipeCategory) = Recipe(
        id = id,
        slug = nameEn.lowercase().replace(" ", "_"),
        name = LocalizedText(en = nameEn, hi = "", gu = ""),
        description = LocalizedText(en = "", hi = "", gu = ""),
        category = category,
        tags = emptyList(),
        prepTimeMinutes = 10,
        cookTimeMinutes = 15,
        servings = 2,
        difficulty = Difficulty.EASY,
        isFarali = false,
        isFestivalSpecial = false,
        isPopular = false,
        ingredients = LocalizedList(emptyList(), emptyList(), emptyList()),
        instructions = LocalizedList(emptyList(), emptyList(), emptyList()),
        notes = null,
        imageUrl = null
    )
}
