package app.chintan.prasadam.di

import app.chintan.prasadam.data.repository.RecipeRepositoryImpl
import app.chintan.prasadam.data.repository.UserPreferencesRepositoryImpl
import app.chintan.prasadam.domain.repository.RecipeRepository
import app.chintan.prasadam.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        impl: RecipeRepositoryImpl
    ): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
