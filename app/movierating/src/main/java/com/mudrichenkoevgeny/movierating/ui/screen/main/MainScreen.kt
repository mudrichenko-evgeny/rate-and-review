package com.mudrichenkoevgeny.movierating.ui.screen.main

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.rememberNavController
import com.mudrichenkoevgeny.core.common.util.findActivity
import com.mudrichenkoevgeny.core.ui.component.bottomnavigationbar.BottomNavigationBar
import com.mudrichenkoevgeny.movierating.ui.navigation.AppNavGraph
import com.mudrichenkoevgeny.movierating.ui.navigation.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val themeUiSettings: ThemeUiSettings = defaultThemeUiSettings()
    SetSystemBarColors(themeUiSettings)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (themeUiSettings.isBottomNavigationBarVisible) {
                BottomNavigationBar(
                    modifier = Modifier
                        .background(themeUiSettings.bottomNavigationBarBackgroundColor),
                    navController = navController,
                    items = BottomNavItem.getAllItems(),
                    backgroundColor = themeUiSettings.bottomNavigationBarBackgroundColor,
                    selectedItemColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
fun SetSystemBarColors(themeUiSettings: ThemeUiSettings) {
    LocalView.current.context.findActivity()?.window?.let { window ->
        val backgroundColor = themeUiSettings.screenBackgroundColor.toArgb()
        window.statusBarColor = backgroundColor
        val navigationBarColor = if (themeUiSettings.isBottomNavigationBarVisible) {
            themeUiSettings.bottomNavigationBarBackgroundColor.toArgb()
        } else {
            backgroundColor
        }
        window.navigationBarColor = navigationBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = navigationBarColor
        }
    }
}

data class ThemeUiSettings(
    val isBottomNavigationBarVisible: Boolean,
    val screenBackgroundColor: Color,
    val bottomNavigationBarBackgroundColor: Color
)

@Composable
fun defaultThemeUiSettings() = ThemeUiSettings(
    isBottomNavigationBarVisible = true,
    screenBackgroundColor = MaterialTheme.colorScheme.background,
    bottomNavigationBarBackgroundColor = MaterialTheme.colorScheme.surface
)