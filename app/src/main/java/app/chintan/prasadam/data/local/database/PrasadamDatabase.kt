package app.chintan.prasadam.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.chintan.prasadam.data.local.dao.RecipeDao
import app.chintan.prasadam.data.local.entity.RecipeEntity

/**
 * Room database for the Prasadam Recipes app.
 *
 * Version history:
 *   1 — Initial schema with recipes table.
 *
 * TODO: Add migration strategies when schema evolves (e.g. auto-migrations via @AutoMigration).
 */
@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PrasadamDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        const val DATABASE_NAME = "prasadam_recipes.db"
    }
}
