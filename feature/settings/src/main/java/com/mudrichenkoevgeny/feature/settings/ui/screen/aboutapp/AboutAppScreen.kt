package com.mudrichenkoevgeny.feature.settings.ui.screen.aboutapp

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.core.ui.R as UIResources
import com.mudrichenkoevgeny.feature.settings.R

@Suppress("DEPRECATION")
@Composable
fun AboutAppScreen(
    args: AboutAppArgs,
    onNavigateToUserAgreement: () -> Unit = { },
    viewModel: AboutAppViewModel = hiltViewModel()
) {
    LaunchedEffect(args) {
        viewModel.onReceivedAppDetails(args)
    }

    val screenUiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val applicationInfo: ApplicationInfo? = remember {
        runCatching {
            context.packageManager.getApplicationInfo(context.packageName, 0)
        }.getOrNull()
    }
    val packageInfo: PackageInfo? = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }.getOrNull()
    }

    val appIcon: Drawable? = remember {
        applicationInfo?.let {
            runCatching { context.packageManager.getApplicationIcon(applicationInfo) }.getOrNull()
        }
    }
    val appName: String? = remember {
        applicationInfo?.let {
            runCatching { context.packageManager.getApplicationLabel(applicationInfo).toString() }.getOrNull()
        }
    }
    val appVersionName: String? = remember { packageInfo?.versionName }
    val appVersionCode: String? = remember {
        packageInfo?.let {
            runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode.toString()
                } else {
                    packageInfo.versionCode.toString()
                }
            }.getOrNull()
        }
    }

    AboutAppScreenUI(
        screenUiState = screenUiState,
        appIcon = appIcon,
        appName = appName,
        appVersionName = appVersionName,
        appVersionCode = appVersionCode,
        onNavigateToUserAgreement = onNavigateToUserAgreement
    )
}

@Composable
private fun AboutAppScreenUI(
    screenUiState: AboutAppUiState,
    appIcon: Drawable?,
    appName: String?,
    appVersionName: String?,
    appVersionCode: String?,
    onNavigateToUserAgreement: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (screenUiState) {
            is AboutAppUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(dimensionResource(UIResources.dimen.padding_base))
                        .align(Alignment.Center)
                        .size(dimensionResource(UIResources.dimen.action_button_image_size_default))
                )
            }
            is AboutAppUiState.Default -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(dimensionResource(UIResources.dimen.component_padding_large)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (appIcon != null) {
                            Image(
                                modifier = Modifier
                                    .size(dimensionResource(UIResources.dimen.profile_avatar_size))
                                    .clip(CircleShape)
                                    .border(
                                        dimensionResource(UIResources.dimen.profile_avatar_border_width),
                                        MaterialTheme.colorScheme.onPrimary,
                                        CircleShape
                                    ),
                                painter = rememberDrawablePainter(appIcon),
                                contentDescription = stringResource(R.string.cd_app_icon)
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    dimensionResource(UIResources.dimen.component_padding_default)
                                )
                            )
                        }
                        if (!appName.isNullOrBlank()) {
                            Text(text = appName)
                        }
                        if (!appVersionName.isNullOrBlank() && !appVersionCode.isNullOrBlank()) {
                            Text(
                                text = stringResource(
                                    R.string.about_app_version_and_code,
                                    appVersionName,
                                    appVersionCode
                                )
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(
                                dimensionResource(UIResources.dimen.component_padding_large)
                            )
                        )
                        Text(text = screenUiState.appDescription)
                    }
                    if (screenUiState.hasUserAgreement()) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onNavigateToUserAgreement
                        ) {
                            Text(stringResource(R.string.about_app_user_agreement))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutAppScreenDefaultPreview() {
    Theme {
        AboutAppScreenUI(
            screenUiState = AboutAppUiState.Default(
                appDescription = MockConstants.MOCK_TEXT_LARGE,
                userAgreementUrl = MockConstants.MOCK_TEXT_SMALL
            ),
            appIcon = null,
            appName = MockConstants.MOCK_TEXT_SMALL,
            appVersionName = "1.0",
            appVersionCode = "1",
            onNavigateToUserAgreement = { }
        )
    }
}