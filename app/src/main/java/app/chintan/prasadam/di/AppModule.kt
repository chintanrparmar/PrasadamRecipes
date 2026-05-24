package app.chintan.prasadam.di

/**
 * App-level Hilt module.
 *
 * [RecipeJsonParser] receives its [android.content.Context] directly via
 * [@ApplicationContext][dagger.hilt.android.qualifiers.ApplicationContext]
 * constructor injection — no explicit binding needed here.
 *
 * TODO: Add remote API client (Retrofit/Ktor) binding here when a backend is ready.
 */
