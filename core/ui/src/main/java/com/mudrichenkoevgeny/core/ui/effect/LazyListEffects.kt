package com.mudrichenkoevgeny.core.ui.effect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun OnScrolledToBottomEffect(
    lazyListState: LazyListState,
    itemsList: List<Any>,
    loadNextPageCondition: (lastVisibleIndex: Int?) -> Boolean,
    onNeedToLoadNextPage: () -> Unit
) {
    LaunchedEffect(lazyListState, itemsList) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (loadNextPageCondition(lastVisibleIndex)) {
                    onNeedToLoadNextPage()
                }
            }
    }
}