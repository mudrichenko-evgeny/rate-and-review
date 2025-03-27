package com.mudrichenkoevgeny.feature.settings.ui.screen.appearancemodepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mudrichenkoevgeny.core.common.enums.AppearanceMode
import com.mudrichenkoevgeny.core.ui.R as UIResources
import com.mudrichenkoevgeny.feature.settings.R
import com.mudrichenkoevgeny.feature.settings.util.getName

@Composable
fun AppearanceModePickerScreen(
    viewModel: AppearanceModePickerViewModel = hiltViewModel()
) {
    val screenUiState by viewModel.uiState.collectAsState()

    AppearanceModePickerScreenUI(
        screenUiState = screenUiState,
        onAppearanceModePicked = { appearanceMode ->
            viewModel.onAppearanceModePicked(appearanceMode)
        }
    )
}

@Composable
private fun AppearanceModePickerScreenUI(
    screenUiState: AppearanceModePickerUiState,
    onAppearanceModePicked: (AppearanceMode) -> Unit
) {
    if (screenUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(dimensionResource(UIResources.dimen.padding_base))
                    .align(Alignment.Center)
                    .size(dimensionResource(UIResources.dimen.action_button_image_size_default))
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = dimensionResource(UIResources.dimen.component_padding_large)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.appearance_mode_picker_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom =dimensionResource(UIResources.dimen.component_padding_large))
            )

            AppearanceMode.entries.forEach { appearanceMode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (appearanceMode == screenUiState.pickedAppearanceMode),
                            onClick = { onAppearanceModePicked(appearanceMode) },
                            role = Role.RadioButton
                        )
                        .padding(dimensionResource(UIResources.dimen.component_padding_default)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (appearanceMode == screenUiState.pickedAppearanceMode),
                        onClick = { onAppearanceModePicked(appearanceMode) }
                    )
                    Text(
                        text = stringResource(appearanceMode.getName()),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = dimensionResource(UIResources.dimen.component_padding_default))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppearanceModePickerScreenDefaultPreview() {
    AppearanceModePickerScreenUI(
        screenUiState = AppearanceModePickerUiState(
            pickedAppearanceMode = AppearanceMode.LIGHT,
            isLoading = false
        ),
        onAppearanceModePicked = { }
    )
}