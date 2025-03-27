package com.mudrichenkoevgeny.core.ui.component.circlebutton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.action_button_image_size_default),
    isVisible: Boolean = true,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    @DrawableRes actionButtonIcon: Int,
    @StringRes actionButtonContentDescription: Int,
    onClicked: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(AbsoluteRoundedCornerShape(size))
            .then(
                if (!isVisible) {
                    Modifier.alpha(0f)
                } else {
                    Modifier
                }
            )
            .background(
                color = when {
                    !isVisible -> Color.Transparent
                    !isEnabled -> MaterialTheme.colorScheme.surfaceVariant
                    else -> MaterialTheme.colorScheme.primary
                },
                shape = CircleShape
            )
            .clickable(enabled = isEnabled && isVisible && !isLoading) {
                onClicked()
            }
    ) {
        if (isVisible) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.component_padding_small)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = dimensionResource(R.dimen.circular_progress_indicator_stroke_width),
                )
            } else {
                Image(
                    painter = painterResource(actionButtonIcon),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .size(size)
                        .padding(dimensionResource(R.dimen.component_inner_image_padding)),
                    contentDescription = stringResource(actionButtonContentDescription),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleButtonDefaultPreview() {
    Theme {
        CircleButton(
            actionButtonIcon = R.drawable.ic_close,
            actionButtonContentDescription = R.string.cd_close
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleButtonInvisiblePreview() {
    Theme {
        CircleButton(
            isVisible = false,
            actionButtonIcon = R.drawable.ic_close,
            actionButtonContentDescription = R.string.cd_close
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleButtonLoadingPreview() {
    Theme {
        CircleButton(
            isLoading = true,
            actionButtonIcon = R.drawable.ic_close,
            actionButtonContentDescription = R.string.cd_close
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleButtonDisabledPreview() {
    Theme {
        CircleButton(
            isEnabled = false,
            actionButtonIcon = R.drawable.ic_close,
            actionButtonContentDescription = R.string.cd_close
        )
    }
}