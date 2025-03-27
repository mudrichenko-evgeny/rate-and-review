package com.mudrichenkoevgeny.core.ui.component.loadingbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.theme.Theme

const val LOADING_BUTTON_CIRCULAR_PROGRESS_INDICATOR_TEST_TAG = "CircularProgressIndicator"

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String = "LoadingButton",
    state: LoadingButtonState = LoadingButtonState.ENABLED,
    onClick: () -> Unit = { }
) {
    Button(
        onClick = onClick,
        enabled = state == LoadingButtonState.ENABLED,
        modifier = modifier
    ) {
        when (state) {
            LoadingButtonState.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.action_button_image_size_default))
                        .testTag(LOADING_BUTTON_CIRCULAR_PROGRESS_INDICATOR_TEST_TAG),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = dimensionResource(R.dimen.circular_progress_indicator_stroke_width),
                )
            }
            LoadingButtonState.SUCCESSFUL -> {
                Image(
                    painter = painterResource(R.drawable.ic_successful),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier.size(dimensionResource(R.dimen.action_button_image_size_default)),
                    contentDescription = stringResource(R.string.cd_successful)
                )
            }
            LoadingButtonState.ENABLED, LoadingButtonState.DISABLED -> {
                Text(text)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingButtonEnabledPreview() {
    Theme {
        LoadingButton(state = LoadingButtonState.ENABLED)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingButtonDisabledPreview() {
    Theme {
        LoadingButton(state = LoadingButtonState.DISABLED)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingButtonLoadingPreview() {
    Theme {
        LoadingButton(state = LoadingButtonState.LOADING)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingButtonSuccessfulPreview() {
    Theme {
        LoadingButton(state = LoadingButtonState.SUCCESSFUL)
    }
}