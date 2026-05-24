package app.chintan.prasadam.di

import android.content.Context
import androidx.room.Room
import app.chintan.prasadam.data.local.dao.RecipeDao
import app.chintan.prasadam.data.local.database.PrasadamDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Suppress("DEPRECATION") // fallbackToDestructiveMigration() is deprecated; acceptable for now — add @AutoMigration when schema changes
    @Provides
    @Singleton
    fun providePrasadamDatabase(
        @ApplicationContext context: Context
    ): PrasadamDatabase = Room.databaseBuilder(
        context,
        PrasadamDatabase::class.java,
        PrasadamDatabase.DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideRecipeDao(database: PrasadamDatabase): RecipeDao = database.recipeDao()
}
