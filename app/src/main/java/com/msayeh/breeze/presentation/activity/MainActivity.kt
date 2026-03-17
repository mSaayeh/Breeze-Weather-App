package com.msayeh.breeze.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.presentation.common.navbar.MainBottomBar
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogContainer
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogState
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.addalerts.ui.AddAlertScreen
import com.msayeh.breeze.presentation.screens.addcity.ui.AddCityScreen
import com.msayeh.breeze.presentation.screens.alerts.ui.AlertsScreen
import com.msayeh.breeze.presentation.screens.cities.ui.CitiesScreen
import com.msayeh.breeze.presentation.screens.home.ui.HomeScreen
import com.msayeh.breeze.presentation.screens.settings.ui.SettingsScreen
import com.msayeh.breeze.presentation.theme.BreezeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val appLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

            LaunchedEffect(appLanguage?.code) {
                val languageCode = appLanguage?.code ?: return@LaunchedEffect
                val requestedLocales = LocaleListCompat.forLanguageTags(languageCode)
                if (AppCompatDelegate.getApplicationLocales() != requestedLocales) {
                    AppCompatDelegate.setApplicationLocales(requestedLocales)
                }
            }
            App(isDarkTheme, appLanguage)
        }
    }
}

val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }
val LocalDialogState = staticCompositionLocalOf { BreezeDialogState() }

fun shouldShowBottomBar(currentBackStackEntry: NavBackStackEntry?): Boolean {
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomBarRoutes = setOf(
        Route.Home::class.qualifiedName,
        Route.Settings::class.qualifiedName,
        Route.Alerts::class.qualifiedName,
    )
    return bottomBarRoutes.any { currentRoute?.startsWith(it ?: "") == true }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(isDarkTheme: Boolean?, appLanguage: AppLanguage?) {
    BreezeTheme(
        darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
        appLanguage = appLanguage,
    ) {
        val navController = rememberNavController()
        val navigateToRouteLambda = { route: Route -> navController.navigate(route) }

        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        CompositionLocalProvider(
            LocalSnackbarHost provides remember { SnackbarHostState() },
            LocalDialogState provides remember { BreezeDialogState() }
        ) {
            BreezeDialogContainer(LocalDialogState.current) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(LocalSnackbarHost.current) },
                    bottomBar = {
                        AnimatedVisibility(
                            shouldShowBottomBar(currentBackStackEntry),
                            enter = fadeIn(tween()) + slideInVertically(
                                tween(),
                                initialOffsetY = { it / 2 }),
                            exit = fadeOut(tween()) + slideOutVertically(
                                tween(),
                                targetOffsetY = { it / 2 })
                        ) {
                            MainBottomBar(navController)
                        }
                    },
                ) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController,
                        startDestination = Route.Home,
                    ) {
                        composable<Route.Home> {
                            HomeScreen(navigateToRouteLambda)
                        }
                        composable<Route.Settings> {
                            SettingsScreen()
                        }
                        composable<Route.Alerts> {
                            AlertsScreen(navigateToRouteLambda)
                        }
                        composable<Route.Cities> {
                            CitiesScreen(navigateToRouteLambda, navController::navigateUp)
                        }
                        composable<Route.AddCity> {
                            AddCityScreen(navController::navigateUp)
                        }
                        composable<Route.AddAlert> {
                            AddAlertScreen(
                                navigateUp = navController::navigateUp,
                                navigateToRoute = navigateToRouteLambda,
                            )
                        }
                    }
                }
            }
        }
    }
}
