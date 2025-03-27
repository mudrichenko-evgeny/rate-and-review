package com.mudrichenkoevgeny.feature.user.ui.screen.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.feature.user.ui.screen.authmain.AuthBottomSheet
import com.mudrichenkoevgeny.core.ui.dialog.alert.ConfirmationDialog
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.feature.user.model.data.getMockUserData
import com.mudrichenkoevgeny.core.ui.R as UIResources

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToUserSettings: () -> Unit,
    onNavigateToAppearanceMode: () -> Unit,
    onNavigateToAboutApp: () -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsState()

    val showAuthDialog = remember { mutableStateOf(false) }
    if (showAuthDialog.value) {
        AuthBottomSheet(
            onDismiss = { showAuthDialog.value = false }
        )
    }

    val showLogoutConfirmationDialog = remember { mutableStateOf(false) }
    if (showLogoutConfirmationDialog.value) {
        ConfirmationDialog(
            title = null,
            text = stringResource(R.string.profile_logout_confirmation_text),
            confirmButtonText = stringResource(R.string.profile_logout_confirmation_yes),
            dismissButtonText = stringResource(R.string.profile_logout_confirmation_no),
            onDismiss = { showLogoutConfirmationDialog.value = false },
            onConfirm = {
                viewModel.onLogoutClicked()
                showLogoutConfirmationDialog.value = false
            }
        )
    }

    ProfileScreenUI(
        screenUiState = screenUiState,
        onLoginClicked = { showAuthDialog.value = true },
        onLogoutClicked = { showLogoutConfirmationDialog.value = true },
        onUserSettingsClicked = onNavigateToUserSettings,
        onAppearanceModeClicked = onNavigateToAppearanceMode,
        onAboutAppClicked = onNavigateToAboutApp
    )
}

@Composable
private fun ProfileScreenUI(
    screenUiState: ProfileUiState,
    onLoginClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onUserSettingsClicked: () -> Unit,
    onAppearanceModeClicked: () -> Unit,
    onAboutAppClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top
        if (screenUiState.isUserAuthorized()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(UIResources.dimen.component_padding_large)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(screenUiState.userData?.avatarUrl)
                        .build(),
                    placeholder = painterResource(UIResources.drawable.ic_avatar),
                    error = painterResource(UIResources.drawable.ic_avatar),
                    contentDescription = stringResource(R.string.cd_profile_user_avatar),
                    modifier = Modifier
                        .size(dimensionResource(UIResources.dimen.profile_avatar_size))
                        .clip(CircleShape)
                        .border(
                            dimensionResource(UIResources.dimen.profile_avatar_border_width),
                            MaterialTheme.colorScheme.onPrimary,
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.component_padding_default)))
                Text(text = screenUiState.userData?.name ?: "")
                Text(text = screenUiState.userData?.email ?: "")
            }
        }
        // Scrollable
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(dimensionResource(UIResources.dimen.component_padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(UIResources.dimen.component_padding_medium))
        ) {
            if (!screenUiState.isUserAuthorized()) {
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onLoginClicked
                    ) {
                        Text(stringResource(R.string.profile_login))
                    }
                }
            }
            if (screenUiState.isUserAuthorized()) {
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onUserSettingsClicked
                    ) {
                        Text(stringResource(R.string.profile_user_settings))
                    }
                }
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAppearanceModeClicked
                ) {
                    Text(stringResource(R.string.profile_appearance_mode))
                }
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAboutAppClicked
                ) {
                    Text(stringResource(R.string.profile_about_app))
                }
            }
            if (screenUiState.isUserAuthorized()) {
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onLogoutClicked
                    ) {
                        Text(stringResource(R.string.profile_logout))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenUserNotAuthorizedPreview() {
    Theme {
        ProfileScreenUI(
            screenUiState = ProfileUiState(
                userData = null
            ),
            onLoginClicked = { },
            onLogoutClicked = { },
            onUserSettingsClicked = { },
            onAppearanceModeClicked = { },
            onAboutAppClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenUserAuthorizedPreview() {
    Theme {
        ProfileScreenUI(
            screenUiState = ProfileUiState(
                userData = getMockUserData()
            ),
            onLoginClicked = { },
            onLogoutClicked = { },
            onUserSettingsClicked = { },
            onAppearanceModeClicked = { },
            onAboutAppClicked = { }
        )
    }
}