package com.mudrichenkoevgeny.core.ui.component.lazylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.mudrichenkoevgeny.core.ui.R

@Composable
fun BaseLazyColumn(
    listState: LazyListState,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            dimensionResource(R.dimen.lazy_column_content_padding)
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.lazy_column_vertical_arrangement_space)
        ),
    ) {
        content()
    }
}