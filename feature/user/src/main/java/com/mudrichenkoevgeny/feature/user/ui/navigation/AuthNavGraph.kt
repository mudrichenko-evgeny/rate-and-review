package com.mudrichenkoevgeny.feature.user.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mudrichenkoevgeny.feature.user.ui.screen.forgotpassword.ForgotPasswordScreen
import com.mudrichenkoevgeny.feature.user.ui.screen.login.LoginScreen
import com.mudrichenkoevgeny.feature.user.ui.screen.registration.RegistrationScreen

const val ROUTE_LOGIN = "login"
const val ROUTE_REGISTER = "register"
const val ROUTE_FORGOT_PASSWORD = "forgotPassword"

@Composable
fun AuthNavGraph(navController: NavHostController, onDismissRequest: () -> Unit) {
    NavHost(navController = navController, startDestination = ROUTE_LOGIN) {
        composable(ROUTE_LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(ROUTE_REGISTER) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(ROUTE_FORGOT_PASSWORD)
                },
                onDismissRequest = onDismissRequest
            )
        }
        composable(ROUTE_REGISTER) {
            RegistrationScreen(
                onNavigateToLogin = {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onDismissRequest = onDismissRequest
            )
        }
        composable(ROUTE_FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onDismissRequest = onDismissRequest
            )
        }
    }
}
