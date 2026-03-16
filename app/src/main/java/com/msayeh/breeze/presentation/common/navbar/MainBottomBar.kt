package com.msayeh.breeze.presentation.common.navbar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.example.bottombar.model.VisibleItem
import com.msayeh.breeze.R
import com.msayeh.breeze.presentation.navigation.Route
import kotlin.reflect.KClass

private enum class NavBarItem(
    val route: Route,
    val routeClass: KClass<out Route>,
    @param:StringRes val label: Int,
    val icon: ImageVector
) {
    Alerts(Route.Alerts, Route.Alerts::class, R.string.alerts, Icons.Default.Alarm),
    Home(Route.Home, Route.Home::class, R.string.home, Icons.Default.Home),
    Settings(
        Route.Settings,
        Route.Settings::class,
        R.string.settings,
        Icons.Default.Settings
    ),
}

private fun NavBackStackEntry?.matchesItem(item: NavBarItem): Boolean {
    return this?.destination?.hasRoute(item.routeClass) == true
}

@Composable
fun MainBottomBar(
    controller: NavHostController,
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        BottomBarContent(controller, modifier = Modifier
            .fillMaxWidth(0.8f)
            .dropShadow(
                CircleShape,
                Shadow(3.dp, offset = DpOffset(0.dp, 3.dp), alpha = 0.1f)
            ))
    }
}

@Composable
private fun BottomBarContent(controller: NavHostController, modifier: Modifier = Modifier) {
    val navBackStackEntry by controller.currentBackStackEntryAsState()

    val selectedNavItem = NavBarItem.entries
        .firstOrNull { navBackStackEntry.matchesItem(it) }
        ?: NavBarItem.Home

    AnimatedBottomBar(
        selectedItem = selectedNavItem.ordinal,
        itemSize = NavBarItem.entries.size,
        indicatorStyle = IndicatorStyle.WORM,
        containerShape = CircleShape,
        modifier = modifier,
    ) {
        NavBarItem.entries.forEach { navBarItem ->
            BottomBarItem(
                selected = navBarItem == selectedNavItem,
                visibleItem = VisibleItem.BOTH,
                itemStyle = ItemStyle.STYLE4,
                onClick = {
                    if (navBarItem != selectedNavItem) {
                        controller.navigate(navBarItem.route) {
                            popUpTo(controller.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                imageVector = navBarItem.icon,
                label = stringResource(navBarItem.label),
                containerColor = Color.Transparent
            )
        }
    }
}