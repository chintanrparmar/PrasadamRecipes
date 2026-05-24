package app.chintan.prasadam.core.navigation

/**
 * Type-safe navigation route definitions.
 * Routes are plain strings consumed by Navigation Compose.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Recipes : Screen("recipes")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")

    /** Detail route with a required [recipeId] argument. */
    data object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun createRoute(recipeId: Int) = "recipe_detail/$recipeId"
    }
}

/** Bottom navigation items shown in the main scaffold. */
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Recipes,
    Screen.Favorites,
    Screen.Settings
)
