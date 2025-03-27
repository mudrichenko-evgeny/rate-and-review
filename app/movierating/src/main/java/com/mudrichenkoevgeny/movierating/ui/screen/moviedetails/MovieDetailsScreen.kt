package com.mudrichenkoevgeny.movierating.ui.screen.moviedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mudrichenkoevgeny.core.model.domain.getMockReview
import com.mudrichenkoevgeny.core.ui.component.basecard.BaseCard
import com.mudrichenkoevgeny.core.ui.component.errorcard.ErrorCard
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorText
import com.mudrichenkoevgeny.core.ui.component.lazylist.BaseLazyColumn
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButtonState
import com.mudrichenkoevgeny.core.ui.component.loadingcard.LoadingCard
import com.mudrichenkoevgeny.core.ui.component.reviewcard.ReviewCard
import com.mudrichenkoevgeny.core.ui.component.reviewtext.EditableReviewText
import com.mudrichenkoevgeny.core.ui.dialog.alert.ConfirmationDialog
import com.mudrichenkoevgeny.core.ui.dialog.editreview.EditReviewDialog
import com.mudrichenkoevgeny.core.ui.dialog.editreview.EditReviewDialogSettings
import com.mudrichenkoevgeny.core.ui.effect.OnScrolledToBottomEffect
import com.mudrichenkoevgeny.feature.user.ui.screen.authmain.AuthBottomSheet
import com.mudrichenkoevgeny.movierating.R
import com.mudrichenkoevgeny.movierating.config.AppConfig
import com.mudrichenkoevgeny.movierating.model.data.getMockMovie
import com.mudrichenkoevgeny.movierating.result.Result
import com.mudrichenkoevgeny.movierating.result.convertToText
import com.mudrichenkoevgeny.movierating.ui.component.movieinfobox.MovieInfoBoxWithGenresList
import com.mudrichenkoevgeny.movierating.ui.theme.AppTheme
import com.mudrichenkoevgeny.core.ui.R as UIResources

@Composable
fun MovieDetailsScreen(
    movieId: String,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(movieId) {
        viewModel.onLoadMovieDetails(movieId)
    }

    val screenUiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val showAuthDialog = remember { mutableStateOf(false) }
    if (showAuthDialog.value) {
        AuthBottomSheet(
            onDismiss = { showAuthDialog.value = false }
        )
    }

    val showEditReviewDialog = remember { mutableStateOf(false) }
    if (showEditReviewDialog.value) {
        EditReviewDialog(
            editReviewDialogSettings = EditReviewDialogSettings(
                maxRating = AppConfig.MAX_RATING
            ),
            userRating = screenUiState.getEditReviewRating(),
            userReviewText = screenUiState.getEditReviewText(),
            onSave = { result ->
                viewModel.saveUserReview(
                    rating = result.rating,
                    reviewText = result.reviewText
                )
                showEditReviewDialog.value = false
            },
            onDismiss = { result ->
                viewModel.cacheUserReview(
                    rating = result.rating,
                    reviewText = result.reviewText
                )
                showEditReviewDialog.value = false
            }
        )
    }

    val showDeleteReviewConfirmationDialog = remember { mutableStateOf(false) }
    if (showDeleteReviewConfirmationDialog.value) {
        ConfirmationDialog(
            title = null,
            text = stringResource(UIResources.string.dialog_delete_review_confirmation_message),
            confirmButtonText = stringResource(UIResources.string.dialog_delete_review_confirmation_yes),
            dismissButtonText = stringResource(UIResources.string.dialog_delete_review_confirmation_no),
            onDismiss = { showDeleteReviewConfirmationDialog.value = false },
            onConfirm = {
                viewModel.onDeleteReviewClicked()
                showDeleteReviewConfirmationDialog.value = false
            }
        )
    }

    MovieDetailsScreenUI(
        screenUiState = screenUiState,
        listState = listState,
        onDeleteReviewClicked = {
            showDeleteReviewConfirmationDialog.value = true
        },
        onEditReviewClicked = {
            viewModel.onEditReviewClicked()
        },
        onRefreshReviewListLoadingClicked = {
            viewModel.loadReviewList()
        }
    )

    OnScrolledToBottomEffect(
        lazyListState = listState,
        itemsList = screenUiState.reviewList,
        loadNextPageCondition = { lastVisibleIndex ->
            screenUiState.isShouldLoadNextPage(lastVisibleIndex)
        },
        onNeedToLoadNextPage = { viewModel.loadReviewList() }
    )

    LaunchedEffect(Unit) {
        viewModel.showAuthDialogRequest.collect { showAuthDialog.value = true }
    }
    LaunchedEffect(Unit) {
        viewModel.showEditReviewDialogRequest.collect { showEditReviewDialog.value = true }
    }
}

@Composable
private fun MovieDetailsScreenUI(
    screenUiState: MovieDetailsUiState,
    listState: LazyListState,
    onDeleteReviewClicked: () -> Unit,
    onEditReviewClicked: () -> Unit,
    onRefreshReviewListLoadingClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            screenUiState.isMovieLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(dimensionResource(UIResources.dimen.padding_base))
                        .align(Alignment.Center)
                        .size(dimensionResource(UIResources.dimen.action_button_image_size_default))
                )
            }
            screenUiState.movieLoadingError != null -> {
                ErrorText(
                    modifier = Modifier
                        .padding(dimensionResource(UIResources.dimen.padding_base))
                        .align(Alignment.Center),
                    text = screenUiState.movieLoadingError.convertToText()
                )
            }
            screenUiState.movie != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MovieInfoBoxWithGenresList(
                        nameAndYear = screenUiState.movie.getNameAndYear(),
                        duration = screenUiState.movie.getFormattedDuration(),
                        tags = screenUiState.movie.getTags(),
                        genres = screenUiState.movie.getGenres(),
                        imageUrl = screenUiState.movie.imageUrl
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(UIResources.dimen.component_padding_large)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when {
                            screenUiState.userReview == null -> {
                                LoadingButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.movie_write_review),
                                    state = if (screenUiState.userReviewEditLoading) {
                                        LoadingButtonState.LOADING
                                    } else {
                                        LoadingButtonState.ENABLED
                                    },
                                    onClick = {
                                        onEditReviewClicked()
                                    }
                                )
                                if (screenUiState.userReviewEditError != null) {
                                    Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
                                    ErrorText(text = screenUiState.userReviewEditError.convertToText())
                                }
                            }
                            else -> {
                                BaseCard {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        EditableReviewText(
                                            isLoading = screenUiState.userReviewEditLoading,
                                            maxRating = AppConfig.MAX_RATING,
                                            rating = screenUiState.userReview.rating,
                                            reviewText = screenUiState.userReview.reviewText,
                                            onDeleteClicked = onDeleteReviewClicked,
                                            onEditClicked = onEditReviewClicked
                                        )
                                        if (screenUiState.userReviewEditError != null) {
                                            Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
                                            ErrorText(text = screenUiState.userReviewEditError.convertToText())
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
                    }
                    BaseLazyColumn(listState = listState) {
                        items(screenUiState.reviewList) { review ->
                            ReviewCard(
                                userName = review.reviewer.name,
                                userAvatarUrl = review.reviewer.avatarUrl,
                                maxRating = AppConfig.MAX_RATING,
                                rating = review.rating,
                                reviewText = review.reviewText
                            )
                        }

                        if (screenUiState.isReviewListLoading) {
                            item {
                                LoadingCard()
                            }
                        }

                        if (screenUiState.reviewListLoadingError != null) {
                            item {
                                ErrorCard(
                                    text = screenUiState.reviewListLoadingError.convertToText(),
                                    isRefreshButtonVisible = true,
                                    onRefreshClicked = { onRefreshReviewListLoadingClicked() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenWithUserReviewPreview() {
    AppTheme {
        MovieDetailsScreenUI(
            screenUiState = MovieDetailsUiState(
                movie = getMockMovie(userReview = null),
                userReview = getMockReview(),
                reviewList = listOf(
                    getMockReview(),
                    getMockReview()
                )
            ),
            listState = rememberLazyListState(),
            onDeleteReviewClicked = { },
            onEditReviewClicked = { },
            onRefreshReviewListLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenMovieLoadingPreview() {
    AppTheme {
        MovieDetailsScreenUI(
            screenUiState = MovieDetailsUiState(
                movie = null,
                isMovieLoading = true,
                movieLoadingError = null
            ),
            listState = rememberLazyListState(),
            onDeleteReviewClicked = { },
            onEditReviewClicked = { },
            onRefreshReviewListLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenMovieLoadingErrorPreview() {
    AppTheme {
        MovieDetailsScreenUI(
            screenUiState = MovieDetailsUiState(
                movie = null,
                isMovieLoading = false,
                movieLoadingError = Result.Failure.Unknown
            ),
            listState = rememberLazyListState(),
            onDeleteReviewClicked = { },
            onEditReviewClicked = { },
            onRefreshReviewListLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenWithUserReviewEditErrorPreview() {
    AppTheme {
        MovieDetailsScreenUI(
            screenUiState = MovieDetailsUiState(
                movie = getMockMovie(userReview = null),
                userReview = getMockReview(),
                userReviewEditError = Result.Failure.Unknown,
                reviewList = listOf(
                    getMockReview(),
                    getMockReview()
                )
            ),
            listState = rememberLazyListState(),
            onDeleteReviewClicked = { },
            onEditReviewClicked = { },
            onRefreshReviewListLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenWithoutUserReviewPreview() {
    AppTheme {
        MovieDetailsScreenUI(
            screenUiState = MovieDetailsUiState(
                movie = getMockMovie(userReview = null),
                userReview = null,
                userReviewEditError = null,
                reviewList = listOf(
                    getMockReview(),
                    getMockReview()
                )
            ),
            listState = rememberLazyListState(),
            onDeleteReviewClicked = { },
            onEditReviewClicked = { },
            onRefreshReviewListLoadingClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenWithoutUserReviewAndWithUserReviewEditErrorPreview() {
    AppTheme {
        MovieDetailsScreenUI(
            screenUiState = MovieDetailsUiState(
                movie = getMockMovie(userReview = null),
                userReview = null,
                userReviewEditError = Result.Failure.Unknown,
                reviewList = listOf(
                    getMockReview(),
                    getMockReview()
                )
            ),
            listState = rememberLazyListState(),
            onDeleteReviewClicked = { },
            onEditReviewClicked = { },
            onRefreshReviewListLoadingClicked = { }
        )
    }
}