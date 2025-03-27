package com.mudrichenkoevgeny.movierating.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mudrichenkoevgeny.core.ui.component.bottomnavigationbar.BottomNavigationItem
import com.mudrichenkoevgeny.core.ui.screen.webview.WebViewScreen
import com.mudrichenkoevgeny.feature.settings.ui.navigation.SettingsNavRoutes.ROUTE_ABOUT_APP
import com.mudrichenkoevgeny.feature.settings.ui.navigation.SettingsNavRoutes.ROUTE_THEME_PICKER
import com.mudrichenkoevgeny.feature.settings.ui.screen.aboutapp.AboutAppArgs
import com.mudrichenkoevgeny.feature.settings.ui.screen.aboutapp.AboutAppScreen
import com.mudrichenkoevgeny.feature.settings.ui.screen.appearancemodepicker.AppearanceModePickerScreen
import com.mudrichenkoevgeny.feature.user.ui.navigation.ProfileNavGraph
import com.mudrichenkoevgeny.feature.user.ui.navigation.ROUTE_PROFILE
import com.mudrichenkoevgeny.movierating.R
import com.mudrichenkoevgeny.movierating.config.AppConfig
import com.mudrichenkoevgeny.movierating.ui.screen.globalfeed.GlobalFeedScreen
import com.mudrichenkoevgeny.movierating.ui.screen.moviedetails.MovieDetailsScreen
import com.mudrichenkoevgeny.movierating.ui.screen.userfeed.UserFeedScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.mudrichenkoevgeny.core.ui.R as UIResources

const val ROUTE_GLOBAL_FEED = "global_feed"
const val ROUTE_USER_FEED = "user_feed"
const val ROUTE_MOVIE_DETAILS = "details"
const val ROUTE_WEB_VIEW = "web_view"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    fun navigateToMovieDetails(movieId: String) {
        navController.navigate("$ROUTE_MOVIE_DETAILS/$movieId")
    }

    fun navigateToWebView(url: String) {
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        navController.navigate("$ROUTE_WEB_VIEW/$encodedUrl")
    }

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.GlobalFeed.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.GlobalFeed.route) {
            GlobalFeedScreen(
                onNavigateToMovieDetails = { movieId ->
                    navigateToMovieDetails(movieId)
                }
            )
        }

        composable(BottomNavItem.UserFeed.route) {
            UserFeedScreen(
                onNavigateToMovieDetails = { movieId ->
                    navigateToMovieDetails(movieId)
                }
            )
        }

        composable(BottomNavItem.Profile.route) {
            ProfileNavGraph(
                onNavigateToAppearanceMode = {
                    navController.navigate(ROUTE_THEME_PICKER)
                },
                onNavigateToAboutApp = {
                    navController.navigate(ROUTE_ABOUT_APP)
                }
            )
        }

        composable(
            route = "$ROUTE_MOVIE_DETAILS/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType})
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: return@composable
            MovieDetailsScreen(
                movieId = movieId
            )
        }

        composable(
            route = "$ROUTE_WEB_VIEW/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType})
        ) { backStackEntry ->
           val encodedUrl = backStackEntry.arguments?.getString("url") ?: return@composable
            val url = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            WebViewScreen(
                url = url
            )
        }

        composable(ROUTE_THEME_PICKER) {
            AppearanceModePickerScreen()
        }

        composable(ROUTE_ABOUT_APP) {
            AboutAppScreen(
                args = AboutAppArgs(
                    appDescription = stringResource(R.string.app_description),
                    userAgreementUrl = AppConfig.USER_AGREEMENT_URL
                ),
                onNavigateToUserAgreement = {
                    navigateToWebView(AppConfig.USER_AGREEMENT_URL)
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val itemRoute: String,
    @StringRes val itemTitleResId: Int,
    @DrawableRes val itemIconResId: Int,
    @StringRes val itemIconContentDescriptionResId: Int
): BottomNavigationItem(
    route = itemRoute,
    titleResId = itemTitleResId,
    iconResId = itemIconResId,
    iconContentDescriptionResId = itemIconContentDescriptionResId
) {
    object GlobalFeed : BottomNavItem(
        itemRoute = ROUTE_GLOBAL_FEED,
        itemTitleResId = R.string.navigation_bar_global_feed,
        itemIconResId = UIResources.drawable.ic_global_feed,
        itemIconContentDescriptionResId = UIResources.string.cd_global_feed
    )
    object UserFeed : BottomNavItem(
        itemRoute = ROUTE_USER_FEED,
        itemTitleResId = R.string.navigation_bar_user_feed,
        itemIconResId = UIResources.drawable.ic_user_feed,
        itemIconContentDescriptionResId = UIResources.string.cd_user_feed
    )
    object Profile : BottomNavItem(
        itemRoute = ROUTE_PROFILE,
        itemTitleResId = R.string.navigation_bar_profile,
        itemIconResId = UIResources.drawable.ic_avatar,
        itemIconContentDescriptionResId = UIResources.string.cd_profile
    )

    companion object {
        fun getAllItems(): List<BottomNavItem> = listOf(GlobalFeed, UserFeed, Profile)
    }
}