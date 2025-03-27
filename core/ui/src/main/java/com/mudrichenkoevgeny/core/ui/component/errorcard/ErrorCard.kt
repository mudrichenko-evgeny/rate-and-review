package com.mudrichenkoevgeny.core.ui.component.errorcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.ui.component.basecard.BaseCard
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorTextWithIcon
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorTextWithRefreshButton
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun ErrorCardWithIcon(
    text: String
) {
    ErrorCard(
        text = text
    )
}

@Composable
fun ErrorCardWithRefreshButton(
    text: String,
    onRefreshClicked: () -> Unit
) {
    ErrorCard(
        text = text,
        isRefreshButtonVisible = true,
        onRefreshClicked = onRefreshClicked
    )
}

@Composable
fun ErrorCard(
    text: String,
    isRefreshButtonVisible: Boolean = false,
    onRefreshClicked: () -> Unit = { }
) {
    BaseCard {
        if (isRefreshButtonVisible) {
            ErrorTextWithRefreshButton(
                text = text,
                onRefreshButtonClicked = onRefreshClicked
            )
        } else {
            ErrorTextWithIcon(
                text = text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorCardWithIconPreview() {
    Theme {
        ErrorCardWithIcon(
            text = "Error message"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorCardWithRefreshButtonPreview() {
    Theme {
        ErrorCardWithRefreshButton(
            text = "Error message",
            onRefreshClicked = { }
        )
    }
}