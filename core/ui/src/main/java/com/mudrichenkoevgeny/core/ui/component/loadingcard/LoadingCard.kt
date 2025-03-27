package com.mudrichenkoevgeny.core.ui.component.loadingcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.component.basecard.BaseCard
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun LoadingCard() {
    BaseCard(
        isEnabled = false
    ) {
        Box(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.card_padding))
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.action_button_image_size_default))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingCardDefaultPreview() {
    Theme {
        LoadingCard()
    }
}