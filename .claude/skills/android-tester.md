---
description: Write comprehensive tests for Prasadam Recipes — unit, UI, and Room integration tests
---

You are an Android testing expert writing robust test suites for the **Prasadam Recipes** app using MockK, Turbine, JUnit 4, and Compose Testing.

## Testing Stack
- **JUnit 4** for unit and instrumented tests
- **MockK 1.14.x** for mocking (Kotlin-idiomatic)
- **Turbine** for `Flow` / `StateFlow` testing
- **Compose Testing** for UI interaction tests
- **Room in-memory** for DAO integration tests
- **Hilt Testing** (`HiltAndroidRule`) for integration tests
- **kotlinx-coroutines-test** with `runTest` and `StandardTestDispatcher`

## Test Directory Layout
```
app/src/test/           # JVM unit tests (ViewModels, UseCases, Repositories, Mapper)
app/src/androidTest/    # Instrumented tests (Room DAO, Compose UI, Hilt integration)
```

## Unit Testing

### ViewModel Testing
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule() // sets Dispatchers.Main to TestDispatcher

    private val getRecipesUseCase: GetRecipesUseCase = mockk(relaxed = true)
    private val getUserPrefsUseCase: GetUserPreferencesUseCase = mockk(relaxed = true)

    private lateinit var viewModel: RecipesViewModel

    @Before
    fun setup() {
        every { getUserPrefsUseCase() } returns flowOf(UserPreferences())
        viewModel = RecipesViewModel(getRecipesUseCase, getUserPrefsUseCase)
    }

    @Test
    fun `recipes are loaded and emitted to uiState`() = runTest {
        // Given
        val recipes = listOf(RecipeTestFixtures.createRecipe(id = 1), RecipeTestFixtures.createRecipe(id = 2))
        every { getRecipesUseCase() } returns flowOf(recipes)

        viewModel = RecipesViewModel(getRecipesUseCase, getUserPrefsUseCase)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.recipes.size)
            assertEquals(1, state.recipes[0].id)
        }
    }

    @Test
    fun `error from use case sets error message in uiState`() = runTest {
        every { getRecipesUseCase() } returns flow { throw IOException("DB error") }

        viewModel = RecipesViewModel(getRecipesUseCase, getUserPrefsUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertNotNull(state.error)
            assertTrue(state.error!!.contains("DB error"))
        }
    }
}
```

### Use Case Testing
```kotlin
class GetRecipesUseCaseTest {

    private val repository: RecipeRepository = mockk()

    @Test
    fun `invoke delegates to repository getAllRecipes`() = runTest {
        val recipes = listOf(RecipeTestFixtures.createRecipe())
        every { repository.getAllRecipes() } returns flowOf(recipes)

        val useCase = GetRecipesUseCase(repository)
        val result = useCase().first()

        assertEquals(1, result.size)
        verify { repository.getAllRecipes() }
    }
}

class ToggleFavoriteRecipeUseCaseTest {

    private val repository: RecipeRepository = mockk(relaxed = true)

    @Test
    fun `invoke calls repository toggleFavorite with correct id`() = runTest {
        coEvery { repository.toggleFavorite(42) } just Runs
        val useCase = ToggleFavoriteRecipeUseCase(repository)
        useCase(42)
        coVerify { repository.toggleFavorite(42) }
    }
}
```

### Repository Testing
```kotlin
class RecipeRepositoryImplTest {

    private val recipeDao: RecipeDao = mockk(relaxed = true)
    private val recipeJsonParser: RecipeJsonParser = mockk()
    private val repository = RecipeRepositoryImpl(recipeDao, recipeJsonParser)

    @Test
    fun `getAllRecipes maps entities to domain models`() = runTest {
        val entities = listOf(RecipeTestFixtures.createEntity(id = 1))
        every { recipeDao.getAllRecipes() } returns flowOf(entities)

        val result = repository.getAllRecipes().first()

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
    }

    @Test
    fun `isDataSeeded returns true when count is greater than zero`() = runTest {
        coEvery { recipeDao.getRecipeCount() } returns 20
        assertTrue(repository.isDataSeeded())
    }

    @Test
    fun `seedFromJson inserts parsed entities`() = runTest {
        val entities = RecipeTestFixtures.createEntityList(3)
        coEvery { recipeJsonParser.parseRecipes() } returns entities
        coEvery { recipeDao.insertAll(any()) } just Runs

        repository.seedFromJson()

        coVerify { recipeDao.insertAll(entities) }
    }
}
```

### Mapper Testing
```kotlin
class RecipeMapperTest {

    @Test
    fun `RecipeEntity maps to Recipe domain model correctly`() {
        val entity = RecipeTestFixtures.createEntity(
            id = 5,
            nameEn = "Sabudana Khichdi",
            nameHi = "साबूदाना खिचड़ी",
            nameGu = "સાબુદાણા ખીચડી",
            category = "FARALI",
            difficulty = "EASY"
        )

        val recipe = entity.toRecipe()

        assertEquals(5, recipe.id)
        assertEquals("Sabudana Khichdi", recipe.name.en)
        assertEquals(RecipeCategory.FARALI, recipe.category)
        assertEquals(Difficulty.EASY, recipe.difficulty)
    }

    @Test
    fun `LocalizedText get returns correct language`() {
        val text = LocalizedText(en = "Milk", hi = "दूध", gu = "દૂધ")

        assertEquals("Milk", text.get(Language.ENGLISH))
        assertEquals("दूध", text.get(Language.HINDI))
        assertEquals("દૂધ", text.get(Language.GUJARATI))
    }

    @Test
    fun `LocalizedText falls back to English when Hindi is blank`() {
        val text = LocalizedText(en = "Ghee", hi = "", gu = "")
        assertEquals("Ghee", text.get(Language.HINDI))
    }
}
```

## Room DAO Integration Tests (instrumented)
```kotlin
@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private lateinit var database: PrasadamDatabase
    private lateinit var dao: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PrasadamDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.recipeDao()
    }

    @After
    fun teardown() = database.close()

    @Test
    fun insertAndRetrieveRecipes() = runTest {
        val entities = RecipeTestFixtures.createEntityList(3)
        dao.insertAll(entities)
        val retrieved = dao.getAllRecipes().first()
        assertEquals(3, retrieved.size)
    }

    @Test
    fun toggleFavorite_flipsIsFavoriteFlag() = runTest {
        dao.insertAll(listOf(RecipeTestFixtures.createEntity(id = 1, isFavorite = false)))
        dao.toggleFavorite(1)
        val after = dao.getRecipeById(1).first()
        assertTrue(after!!.isFavorite)
    }

    @Test
    fun getFavoriteRecipes_returnsOnlyFavourites() = runTest {
        dao.insertAll(listOf(
            RecipeTestFixtures.createEntity(id = 1, isFavorite = true),
            RecipeTestFixtures.createEntity(id = 2, isFavorite = false)
        ))
        val favourites = dao.getFavoriteRecipes().first()
        assertEquals(1, favourites.size)
        assertEquals(1, favourites[0].id)
    }
}
```

## Compose UI Testing
```kotlin
class RecipesScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun recipeList_displaysRecipeNames() {
        val uiState = RecipesUiState(
            recipes = listOf(
                RecipeTestFixtures.createRecipe(id = 1, nameEn = "Methi Thepla"),
                RecipeTestFixtures.createRecipe(id = 2, nameEn = "Gujarati Kadhi")
            ),
            language = Language.ENGLISH
        )

        composeTestRule.setContent {
            PrasadamTheme {
                RecipesContent(uiState = uiState, onRecipeClick = {})
            }
        }

        composeTestRule.onNodeWithText("Methi Thepla").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gujarati Kadhi").assertIsDisplayed()
    }

    @Test
    fun recipeCard_clickTriggersCallback() {
        var clickedId = -1
        val recipe = RecipeTestFixtures.createRecipe(id = 7)
        val uiState = RecipesUiState(recipes = listOf(recipe), language = Language.ENGLISH)

        composeTestRule.setContent {
            PrasadamTheme {
                RecipesContent(uiState = uiState, onRecipeClick = { clickedId = it })
            }
        }

        composeTestRule.onNodeWithTag("recipe_card_7").performClick()
        assertEquals(7, clickedId)
    }

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            PrasadamTheme {
                RecipesContent(uiState = RecipesUiState(isLoading = true), onRecipeClick = {})
            }
        }
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun emptyState_showsEmptyMessage() {
        composeTestRule.setContent {
            PrasadamTheme {
                RecipesContent(uiState = RecipesUiState(recipes = emptyList()), onRecipeClick = {})
            }
        }
        composeTestRule.onNodeWithText("No recipes found").assertIsDisplayed()
    }
}
```

## Test Fixtures
```kotlin
object RecipeTestFixtures {

    fun createRecipe(
        id: Int = 1,
        nameEn: String = "Test Recipe",
        nameHi: String = "परीक्षण रेसिपी",
        nameGu: String = "ટેસ્ટ રેસિપી",
        category: RecipeCategory = RecipeCategory.SNACKS,
        isFarali: Boolean = false,
        isFavorite: Boolean = false
    ) = Recipe(
        id = id,
        slug = "test-recipe-$id",
        name = LocalizedText(en = nameEn, hi = nameHi, gu = nameGu),
        description = LocalizedText("Desc", "विवरण", "વર્ણ"),
        category = category,
        tags = emptyList(),
        prepTimeMinutes = 10,
        cookTimeMinutes = 20,
        servings = 2,
        difficulty = Difficulty.EASY,
        isFarali = isFarali,
        isFestivalSpecial = false,
        isPopular = false,
        ingredients = LocalizedList(listOf("Ingredient 1"), listOf("सामग्री 1"), listOf("સ. 1")),
        instructions = LocalizedList(listOf("Step 1"), listOf("चरण 1"), listOf("પ. 1")),
        notes = null,
        imageUrl = null,
        isFavorite = isFavorite
    )

    fun createRecipeList(count: Int = 5) = List(count) { createRecipe(id = it + 1) }

    fun createEntity(
        id: Int = 1,
        nameEn: String = "Test Recipe",
        nameHi: String = "परीक्षण",
        nameGu: String = "ટેસ્ટ",
        category: String = "SNACKS",
        difficulty: String = "EASY",
        isFavorite: Boolean = false
    ) = RecipeEntity(
        id = id, slug = "test-$id",
        nameEn = nameEn, nameHi = nameHi, nameGu = nameGu,
        descriptionEn = "Desc", descriptionHi = "वर्णन", descriptionGu = "વ.",
        category = category,
        tags = "[]",
        prepTimeMinutes = 10, cookTimeMinutes = 20, servings = 2,
        difficulty = difficulty,
        isFarali = false, isFestivalSpecial = false, isPopular = false,
        ingredientsEn = "[\"item\"]", ingredientsHi = "[\"वस्तु\"]", ingredientsGu = "[\"ₑ\"]",
        instructionsEn = "[\"step\"]", instructionsHi = "[\"चरण\"]", instructionsGu = "[\"ₛ\"]",
        notesEn = null, notesHi = null, notesGu = null,
        imageUrl = null, isFavorite = isFavorite
    )

    fun createEntityList(count: Int) = List(count) { createEntity(id = it + 1) }
}
```

## Test Coverage Targets
| Layer | Target |
|---|---|
| Domain Use Cases | 100% |
| RecipeMapper | 100% |
| ViewModels | ≥ 85% |
| RecipeRepositoryImpl | ≥ 80% |
| Room DAO (instrumented) | ≥ 75% |
| Compose screens | Critical paths |

## Naming Convention
```kotlin
// methodName_condition_expectedResult
fun `getAllRecipes_withEmptyDatabase_returnsEmptyList`()
fun `toggleFavorite_whenAlreadyFavorite_setsFavoriteToFalse`()
fun `localizedText_whenHindiBlank_fallsBackToEnglish`()
```

Remember: test behaviour, not implementation. Keep fixtures minimal and focused.
