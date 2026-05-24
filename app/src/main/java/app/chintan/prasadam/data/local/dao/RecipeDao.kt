package app.chintan.prasadam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.chintan.prasadam.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY nameEn ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY nameEn ASC")
    fun getRecipesByCategory(category: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: Int): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY nameEn ASC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    /**
     * Full-text search across all three language columns for name, tags, and ingredients.
     * The [app.chintan.prasadam.domain.usecase.SearchRecipesUseCase] performs an additional
     * in-memory pass to cover Gujarati/Hindi ingredients stored as JSON arrays.
     */
    @Query(
        """
        SELECT * FROM recipes
        WHERE nameEn      LIKE '%' || :query || '%'
           OR nameHi      LIKE '%' || :query || '%'
           OR nameGu      LIKE '%' || :query || '%'
           OR tags        LIKE '%' || :query || '%'
           OR ingredientsEn LIKE '%' || :query || '%'
           OR ingredientsHi LIKE '%' || :query || '%'
           OR ingredientsGu LIKE '%' || :query || '%'
        ORDER BY nameEn ASC
        """
    )
    fun searchRecipes(query: String): Flow<List<RecipeEntity>>

    @Query("SELECT isFavorite FROM recipes WHERE id = :id")
    suspend fun isFavorite(id: Int): Boolean

    @Query("UPDATE recipes SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(recipes: List<RecipeEntity>)

    @Update
    suspend fun update(recipe: RecipeEntity)

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getCount(): Int
}
