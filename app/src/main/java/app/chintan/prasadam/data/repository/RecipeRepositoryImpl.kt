package app.chintan.prasadam.data.repository

import app.chintan.prasadam.data.local.dao.RecipeDao
import app.chintan.prasadam.data.mapper.toDomain
import app.chintan.prasadam.data.source.RecipeJsonParser
import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.domain.model.RecipeCategory
import app.chintan.prasadam.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
    private val jsonParser: RecipeJsonParser
) : RecipeRepository {

    override fun getAllRecipes(): Flow<List<Recipe>> =
        dao.getAllRecipes().map { entities -> entities.map { it.toDomain() } }

    override fun getRecipesByCategory(category: RecipeCategory): Flow<List<Recipe>> =
        dao.getRecipesByCategory(category.name).map { entities -> entities.map { it.toDomain() } }

    override fun getRecipeById(id: Int): Flow<Recipe?> =
        dao.getRecipeById(id).map { it?.toDomain() }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> =
        dao.getFavoriteRecipes().map { entities -> entities.map { it.toDomain() } }

    override fun searchRecipes(query: String): Flow<List<Recipe>> =
        dao.searchRecipes(query).map { entities -> entities.map { it.toDomain() } }

    override suspend fun toggleFavorite(recipeId: Int) = dao.toggleFavorite(recipeId)

    override suspend fun isDataSeeded(): Boolean = dao.getCount() > 0

    override suspend fun seedFromJson() {
        val entities = jsonParser.parseRecipes()
        dao.insertAll(entities)
    }
}
