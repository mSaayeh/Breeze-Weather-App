package com.msayeh.breeze.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.msayeh.breeze.presentation.common.MainBottomBar
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogContainer
import com.msayeh.breeze.presentation.common.dialog.BreezeDialogState
import com.msayeh.breeze.presentation.navigation.Route
import com.msayeh.breeze.presentation.screens.alerts.AlertsScreen
import com.msayeh.breeze.presentation.screens.home.ui.HomeScreen
import com.msayeh.breeze.presentation.screens.settings.SettingsScreen
import com.msayeh.breeze.presentation.theme.BreezeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initializeLocale()
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsStateWithLifecycle()
            App(isDarkTheme)
        }
    }

    fun initializeLocale() {
        lifecycleScope.launch {
            viewModel.selectedLanguage.collectLatest {
                if (it != null) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(it.code))
                }
            }
        }
    }
}

val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }
val LocalDialogState = compositionLocalOf { BreezeDialogState() }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(isDarkTheme: Boolean?) {
    BreezeTheme(
        darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
    ) {
        val navController = rememberNavController()

        CompositionLocalProvider(
            LocalSnackbarHost provides SnackbarHostState(),
            LocalDialogState provides BreezeDialogState()
        ) {
            BreezeDialogContainer(LocalDialogState.current) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(LocalSnackbarHost.current) },
                    bottomBar = {
                        MainBottomBar(navController)
                    },
                ) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController,
                        startDestination = Route.Home,
                    ) {
                        composable<Route.Home> {
                            HomeScreen()
                        }
                        composable<Route.Settings> {
                            SettingsScreen()
                        }
                        composable<Route.Alerts> {
                            AlertsScreen()
                        }
                    }
                }
            }
        }
    }
}
