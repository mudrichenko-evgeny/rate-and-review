package com.mudrichenkoevgeny.core.ui.component.bottomnavigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mudrichenkoevgeny.core.ui.R

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationBar(
    modifier: Modifier,
    navController: NavHostController,
    items: List<BottomNavigationItem>,
    backgroundColor: Color,
    selectedItemColor: Color
) {
    // for recompose triggering
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route

    val backStack = navController.currentBackStack.value
    val rootRoutes = items.map { bottomNavigationItem ->
        bottomNavigationItem.route
    }
    val currentRootBackStackEntry = backStack.lastOrNull { backStackEntry ->
        backStackEntry.destination.route in rootRoutes
    }

    NavigationBar(
        containerColor = backgroundColor,
        modifier = modifier
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRootBackStackEntry?.destination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(stringResource(item.titleResId)) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.iconResId),
                        contentDescription = stringResource(item.iconContentDescriptionResId),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.bottom_navigation_bar_icon_size))
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = selectedItemColor
                )
            )
        }
    }
}