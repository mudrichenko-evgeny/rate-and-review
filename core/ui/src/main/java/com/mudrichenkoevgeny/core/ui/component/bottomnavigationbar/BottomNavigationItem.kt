package com.mudrichenkoevgeny.core.ui.component.bottomnavigationbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

abstract class BottomNavigationItem(
    val route: String,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
    @StringRes val iconContentDescriptionResId: Int
)