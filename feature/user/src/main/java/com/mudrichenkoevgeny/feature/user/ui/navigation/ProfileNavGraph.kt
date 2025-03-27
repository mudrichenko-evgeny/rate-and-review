package com.mudrichenkoevgeny.feature.user.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mudrichenkoevgeny.feature.user.ui.screen.profile.ProfileScreen
import com.mudrichenkoevgeny.feature.user.ui.screen.usersettings.UserSettingsScreen

const val ROUTE_PROFILE = "profile"
const val ROUTE_PROFILE_HOME = "$ROUTE_PROFILE/home"
const val ROUTE_PROFILE_USER_SETTINGS = "$ROUTE_PROFILE/userSettings"

@Composable
fun ProfileNavGraph(
    onNavigateToAppearanceMode: () -> Unit,
    onNavigateToAboutApp: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = ROUTE_PROFILE_HOME) {
        composable(ROUTE_PROFILE_HOME) {
            ProfileScreen(
                onNavigateToUserSettings = {
                    navController.navigate(ROUTE_PROFILE_USER_SETTINGS)
                },
                onNavigateToAppearanceMode = onNavigateToAppearanceMode,
                onNavigateToAboutApp = onNavigateToAboutApp
            )
        }

        composable(ROUTE_PROFILE_USER_SETTINGS) {
            UserSettingsScreen(
                onCloseScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}