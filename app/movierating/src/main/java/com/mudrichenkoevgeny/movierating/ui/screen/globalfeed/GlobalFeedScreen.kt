package com.mudrichenkoevgeny.movierating.ui.screen.globalfeed

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mudrichenkoevgeny.core.ui.component.errorcard.ErrorCard
import com.mudrichenkoevgeny.core.ui.component.lazylist.BaseLazyColumn
import com.mudrichenkoevgeny.core.ui.component.loadingcard.LoadingCard
import com.mudrichenkoevgeny.core.ui.effect.OnScrolledToBottomEffect
import com.mudrichenkoevgeny.movierating.model.data.getMockMovie
import com.mudrichenkoevgeny.movierating.result.Result
import com.mudrichenkoevgeny.movierating.result.convertToText
import com.mudrichenkoevgeny.movierating.ui.component.moviecard.MovieCard
import com.mudrichenkoevgeny.movierating.ui.theme.AppTheme

@Composable
fun GlobalFeedScreen(
    viewModel: GlobalFeedViewModel = hiltViewModel(),
    onNavigateToMovieDetails: (movieId: String) -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    GlobalFeedScreenUI(
        screenUiState = screenUiState,
        listState = listState,
        onNavigateToMovieDetails = onNavigateToMovieDetails,
        onRefreshLoadingClicked = { viewModel.loadFeed() }
    )

    OnScrolledToBottomEffect(
        lazyListState = listState,
        itemsList = screenUiState.movieList,
        loadNextPageCondition = { lastVisibleIndex ->
            screenUiState.isShouldLoadNextPage(lastVisibleIndex)
        },
        onNeedToLoadNextPage = { viewModel.loadFeed() }
    )
}

@Composable
private fun GlobalFeedScreenUI(
    screenUiState: GlobalFeedUiState,
    listState: LazyListState,
    onNavigateToMovieDetails: (movieId: String) -> Unit,
    onRefreshLoadingClicked: () -> Unit
) {
    BaseLazyColumn(listState = listState) {
        items(screenUiState.movieList) { movie ->
            MovieCard(
                movie = movie,
                onClick = { movieId -> onNavigateToMovieDetails(movieId) }
            )
        }

        if (screenUiState.isLoading) {
            item {
                LoadingCard()
            }
        }

        if (screenUiState.movieListLoadingError != null) {
            item {
                ErrorCard(
                    text = screenUiState.movieListLoadingError.convertToText(),
                    isRefreshButtonVisible = true,
                    onRefreshClicked = { onRefreshLoadingClicked() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GlobalFeedScreenDefaultPreview() {
    AppTheme {
        GlobalFeedScreenUI(
            screenUiState = GlobalFeedUiState(
                movieList = listOf(
                    getMockMovie(userReview = null),
                    getMockMovie(userReview = null)
                ),
                isLoading = false,
                movieListLoadingError = null
            ),
            listState = rememberLazyListState(),
            onNavigateToMovieDetails = { },
            onRefreshLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GlobalFeedScreenPageLoadingPreview() {
    AppTheme {
        GlobalFeedScreenUI(
            screenUiState = GlobalFeedUiState(
                movieList = listOf(
                    getMockMovie(userReview = null),
                    getMockMovie(userReview = null)
                ),
                isLoading = true,
                movieListLoadingError = null
            ),
            listState = rememberLazyListState(),
            onNavigateToMovieDetails = { },
            onRefreshLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GlobalFeedScreenPageErrorPreview() {
    AppTheme {
        GlobalFeedScreenUI(
            screenUiState = GlobalFeedUiState(
                movieList = listOf(
                    getMockMovie(userReview = null),
                    getMockMovie(userReview = null)
                ),
                isLoading = false,
                movieListLoadingError = Result.Failure.Unknown
            ),
            listState = rememberLazyListState(),
            onNavigateToMovieDetails = { },
            onRefreshLoadingClicked = { }
        )
    }
}