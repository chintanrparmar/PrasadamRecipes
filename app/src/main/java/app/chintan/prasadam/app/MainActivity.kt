package app.chintan.prasadam.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.chintan.prasadam.core.design.CardWhite
import app.chintan.prasadam.core.design.FreshGreen
import app.chintan.prasadam.core.design.LightGreenChip
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.PrasadamTheme
import app.chintan.prasadam.core.design.SecondaryText
import app.chintan.prasadam.core.localization.AppStrings
import app.chintan.prasadam.core.navigation.PrasadamNavGraph
import app.chintan.prasadam.core.navigation.Screen
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.feature.settings.SettingsViewModel
import app.chintan.prasadam.feature.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            var splashVisible by rememberSaveable { mutableStateOf(true) }

            PrasadamTheme(
                themePreference = settingsState.selectedTheme,
                language = settingsState.selectedLanguage
            ) {
                if (splashVisible) {
                    SplashScreen(onFinished = { splashVisible = false })
                } else {
                    PrasadamAppScaffold()
                }
            }
        }
    }
}

// ── Root scaffold ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrasadamAppScaffold() {
    val navController = rememberNavController()
    val lang = LocalAppLanguage.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topLevelRoutes = setOf(
        Screen.Home.route,
        Screen.Recipes.route,
        Screen.Favorites.route,
        Screen.Settings.route
    )
    val isTopLevel = currentRoute in topLevelRoutes

    // Home screen uses its own PrasadamHeroCard, so no top bar needed there.
    val showTopBar = isTopLevel && currentRoute != Screen.Home.route

    val topBarTitle = when (currentRoute) {
        Screen.Recipes.route   -> AppStrings.recipes(lang)
        Screen.Favorites.route -> AppStrings.favorites(lang)
        Screen.Settings.route  -> AppStrings.settings(lang)
        else                   -> ""
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if (isTopLevel) {
                PrasadamBottomBar(navController = navController, lang = lang)
            }
        }
    ) { innerPadding ->
        PrasadamNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// ── Bottom navigation bar ─────────────────────────────────────────────────────

private data class BottomNavItem(
    val screen: Screen,
    val labelFn: (Language) -> String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val navItems = listOf(
    BottomNavItem(Screen.Home, AppStrings::home, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Screen.Recipes, AppStrings::recipes, Icons.Filled.List, Icons.Outlined.List),
    BottomNavItem(
        Screen.Favorites,
        AppStrings::favorites,
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder
    ),
    BottomNavItem(
        Screen.Settings,
        AppStrings::settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    )
)

@Composable
private fun PrasadamBottomBar(navController: NavHostController, lang: Language) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = CardWhite,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.labelFn(lang)
                    )
                },
                label = { Text(item.labelFn(lang)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = FreshGreen,
                    selectedTextColor   = FreshGreen,
                    indicatorColor      = LightGreenChip,
                    unselectedIconColor = SecondaryText,
                    unselectedTextColor = SecondaryText
                )
            )
        }
    }
}
