package com.mudrichenkoevgeny.core.ui.component.basecard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun BaseCard(
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        enabled = isEnabled,
        onClick = onClick
    ) {
        Box(
            modifier = modifier.padding(dimensionResource(R.dimen.card_padding))
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseCardDefaultPreview() {
    Theme {
        BaseCard {
            Text(
                text = MockConstants.MOCK_TEXT_LARGE
            )
        }
    }
}