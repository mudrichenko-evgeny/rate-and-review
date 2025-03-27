package com.mudrichenkoevgeny.core.ui.component.errortext

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.component.circlebutton.CircleButton
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun ErrorTextWithIcon(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String
) {
    ErrorText(
        modifier = modifier,
        text = text
    )
}

@Composable
fun ErrorTextWithCloseButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    onCloseButtonClicked: () -> Unit
) {
    ErrorText(
        modifier = modifier,
        text = text,
        isActionButtonVisible = true,
        onActionButtonClicked = onCloseButtonClicked
    )
}

@Composable
fun ErrorTextWithRefreshButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    onRefreshButtonClicked: () -> Unit
) {
    ErrorText(
        modifier = modifier,
        text = text,
        isActionButtonVisible = true,
        actionButtonIcon = R.drawable.ic_refresh,
        actionButtonContentDescription = R.string.cd_refresh,
        onActionButtonClicked = onRefreshButtonClicked
    )
}

@Composable
fun ErrorText(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    @DrawableRes icon: Int? = R.drawable.ic_error,
    isActionButtonVisible: Boolean = false,
    @DrawableRes actionButtonIcon: Int = R.drawable.ic_close,
    @StringRes actionButtonContentDescription: Int = R.string.cd_close,
    onActionButtonClicked: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(45)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.component_padding_large),
                    vertical = dimensionResource(R.dimen.component_padding_default)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.action_button_image_size_default)),
                    contentDescription = stringResource(R.string.cd_error)
                )
                Spacer(
                    modifier = Modifier.width(dimensionResource(R.dimen.action_button_image_size_default))
                )
            }
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.error
            )
            Spacer(
                modifier = Modifier.width(dimensionResource(R.dimen.action_button_image_size_default))
            )
            CircleButton(
                isVisible = isActionButtonVisible,
                actionButtonIcon = actionButtonIcon,
                actionButtonContentDescription = actionButtonContentDescription,
                onClicked = onActionButtonClicked
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorTextWithIconPreview() {
    Theme {
        ErrorTextWithIcon(
            text = "Unknown error"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorTextWithCloseButtonPreview() {
    Theme {
        ErrorTextWithCloseButton(
            text = "Unknown error",
            onCloseButtonClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorTextWithRefreshButtonPreview() {
    Theme {
        ErrorTextWithRefreshButton(
            text = "Unknown error",
            onRefreshButtonClicked = { }
        )
    }
}