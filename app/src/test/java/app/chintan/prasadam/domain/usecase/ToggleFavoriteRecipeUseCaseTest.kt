package app.chintan.prasadam.domain.usecase

import app.chintan.prasadam.domain.repository.RecipeRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleFavoriteRecipeUseCaseTest {

    private val repository: RecipeRepository = mockk()
    private val useCase = ToggleFavoriteRecipeUseCase(repository)

    @Test
    fun `invoke delegates to repository toggleFavorite`() = runTest {
        coJustRun { repository.toggleFavorite(42) }

        useCase(42)

        coVerify(exactly = 1) { repository.toggleFavorite(42) }
    }

    @Test
    fun `invoke can be called multiple times`() = runTest {
        coJustRun { repository.toggleFavorite(any()) }

        useCase(1)
        useCase(1)
        useCase(2)

        coVerify(exactly = 2) { repository.toggleFavorite(1) }
        coVerify(exactly = 1) { repository.toggleFavorite(2) }
    }
}
