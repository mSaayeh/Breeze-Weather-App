package com.msayeh.breeze

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.msayeh.breeze.presentation.Route
import com.msayeh.breeze.presentation.alerts.AlertsScreen
import com.msayeh.breeze.presentation.common.MainBottomBar
import com.msayeh.breeze.presentation.home.ui.HomeScreen
import com.msayeh.breeze.presentation.settings.SettingsScreen
import com.msayeh.breeze.presentation.theme.BreezeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    BreezeTheme {
        val navController = rememberNavController()

        CompositionLocalProvider(LocalSnackbarHost provides SnackbarHostState()) {
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
