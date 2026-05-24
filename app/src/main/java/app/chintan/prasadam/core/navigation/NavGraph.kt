package app.chintan.prasadam.core.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.chintan.prasadam.feature.favorites.FavoritesScreen
import app.chintan.prasadam.feature.home.HomeScreen
import app.chintan.prasadam.feature.recipedetail.RecipeDetailScreen
import app.chintan.prasadam.feature.recipes.RecipesScreen
import app.chintan.prasadam.feature.settings.SettingsScreen

/**
 * Root navigation graph wired to the app's [NavHostController].
 *
 * The NavController is intentionally NOT passed into individual screen composables —
 * callbacks for navigation actions are passed instead, keeping screens stateless
 * and easy to preview / test.
 */
@Composable
fun PrasadamNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onSeeAllClick = { navController.navigate(Screen.Recipes.route) }
            )
        }

        composable(route = Screen.Recipes.route) {
            RecipesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                }
            )
        }

        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(
                navArgument(Screen.RecipeDetail.ARG_RECIPE_ID) { type = NavType.IntType }
            )
        ) {
            RecipeDetailScreen(
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                }
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
