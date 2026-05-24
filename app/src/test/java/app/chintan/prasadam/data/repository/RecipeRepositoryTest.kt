package app.chintan.prasadam.data.repository

import app.chintan.prasadam.data.local.dao.RecipeDao
import app.chintan.prasadam.data.local.entity.RecipeEntity
import app.chintan.prasadam.data.source.RecipeJsonParser
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RecipeRepositoryTest {

    private lateinit var dao: RecipeDao
    private lateinit var jsonParser: RecipeJsonParser
    private lateinit var repository: RecipeRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk()
        jsonParser = mockk()
        repository = RecipeRepositoryImpl(dao, jsonParser)
    }

    @Test
    fun `getAllRecipes returns mapped domain recipes`() = runTest {
        val entities = listOf(fakeEntity(id = 1), fakeEntity(id = 2))
        every { dao.getAllRecipes() } returns flowOf(entities)

        repository.getAllRecipes().test {
            val recipes = awaitItem()
            assertEquals(2, recipes.size)
            assertEquals(1, recipes[0].id)
            assertEquals(2, recipes[1].id)
            awaitComplete()
        }
    }

    @Test
    fun `getFavoriteRecipes returns only favorites`() = runTest {
        val favorites = listOf(fakeEntity(id = 3, isFavorite = true))
        every { dao.getFavoriteRecipes() } returns flowOf(favorites)

        repository.getFavoriteRecipes().test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            assertTrue(recipes.first().isFavorite)
            awaitComplete()
        }
    }

    @Test
    fun `isDataSeeded returns false when db is empty`() = runTest {
        coEvery { dao.getCount() } returns 0
        assertFalse(repository.isDataSeeded())
    }

    @Test
    fun `isDataSeeded returns true when db has records`() = runTest {
        coEvery { dao.getCount() } returns 5
        assertTrue(repository.isDataSeeded())
    }

    @Test
    fun `seedFromJson calls insertAll with parsed entities`() = runTest {
        val entities = listOf(fakeEntity(id = 1))
        every { jsonParser.parseRecipes() } returns entities
        coJustRun { dao.insertAll(any()) }

        repository.seedFromJson()

        coVerify(exactly = 1) { dao.insertAll(entities) }
    }

    @Test
    fun `toggleFavorite delegates to dao`() = runTest {
        coJustRun { dao.toggleFavorite(7) }
        repository.toggleFavorite(7)
        coVerify(exactly = 1) { dao.toggleFavorite(7) }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun fakeEntity(id: Int, isFavorite: Boolean = false) = RecipeEntity(
        id = id,
        slug = "recipe-$id",
        nameEn = "Recipe $id",
        nameHi = "",
        nameGu = "",
        descriptionEn = "",
        descriptionHi = "",
        descriptionGu = "",
        category = "SNACKS",
        tags = "[]",
        prepTimeMinutes = 10,
        cookTimeMinutes = 15,
        servings = 2,
        difficulty = "EASY",
        isFarali = false,
        isFestivalSpecial = false,
        isPopular = false,
        ingredientsEn = "[]",
        ingredientsHi = "[]",
        ingredientsGu = "[]",
        instructionsEn = "[]",
        instructionsHi = "[]",
        instructionsGu = "[]",
        isFavorite = isFavorite
    )
}
