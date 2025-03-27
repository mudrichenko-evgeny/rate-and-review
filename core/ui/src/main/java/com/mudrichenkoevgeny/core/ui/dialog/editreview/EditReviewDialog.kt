package com.mudrichenkoevgeny.core.ui.dialog.editreview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.theme.Theme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReviewDialog(
    editReviewDialogSettings: EditReviewDialogSettings = EditReviewDialogSettings(),
    userRating: Int? = null,
    userReviewText: String = "",
    onSave: (EditReviewDialogResult) -> Unit,
    onDismiss: (EditReviewDialogResult) -> Unit
) {
    var sliderValue = remember { mutableFloatStateOf((userRating ?: editReviewDialogSettings.getDefaultRating()).toFloat()) }
    var reviewText = remember { mutableStateOf(userReviewText) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false }
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss(
                EditReviewDialogResult(
                    rating = sliderValue.floatValue.toInt(),
                    reviewText = reviewText.value
                )
            )
        },
        sheetState = sheetState,
        dragHandle = { }
    ) {
        EditReviewDialogUI(
            editReviewDialogSettings = editReviewDialogSettings,
            userRating = sliderValue.floatValue.toInt(),
            userReviewText = reviewText.value,
            onRatingChanged = { rating ->
                sliderValue.floatValue = rating.toFloat()
            },
            onReviewTextChanged = { text ->
                reviewText.value = text
            },
            onSaveClicked = {
                onSave(
                    EditReviewDialogResult(
                        rating = sliderValue.floatValue.toInt(),
                        reviewText = reviewText.value
                    )
                )
            },
            onDismiss = {
                onDismiss(
                    EditReviewDialogResult(
                        rating = sliderValue.floatValue.toInt(),
                        reviewText = reviewText.value
                    )
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditReviewDialogUI(
    editReviewDialogSettings: EditReviewDialogSettings = EditReviewDialogSettings(),
    userRating: Int,
    userReviewText: String,
    onRatingChanged: (Int) -> Unit,
    onReviewTextChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_base)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.component_padding_medium))
    ) {
        if (!editReviewDialogSettings.title.isNullOrBlank()) {
            Text(editReviewDialogSettings.title, style = MaterialTheme.typography.titleLarge)
        }

        Column(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_base)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.edit_review_rating_label),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$userRating/${editReviewDialogSettings.maxRating}",
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
            Slider(
                value = userRating.toFloat(),
                onValueChange = { onRatingChanged(it.toInt()) },
                valueRange =  editReviewDialogSettings.minRating.toFloat()..editReviewDialogSettings.maxRating.toFloat(),
            )
        }

        OutlinedTextField(
            value = userReviewText,
            onValueChange = { text ->
                if (text.length <= editReviewDialogSettings.reviewTextMaxSymbols) {
                    onReviewTextChanged(text)
                }
            },
            label = { Text(stringResource(R.string.edit_review_text_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            supportingText = {
                Text(
                    text = "${userReviewText.length} / ${editReviewDialogSettings.reviewTextMaxSymbols}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.edit_review_cancel))
            }
            Spacer(
                modifier = Modifier.width(dimensionResource(R.dimen.component_padding_default))
            )
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onSaveClicked()
                }
            ) {
                Text(stringResource(R.string.edit_review_save))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditReviewDialogDefaultPreview() {
    Theme {
        EditReviewDialogUI(
            userRating = 75,
            userReviewText = MockConstants.MOCK_TEXT_LARGE,
            onRatingChanged = { },
            onReviewTextChanged = { },
            onSaveClicked = { },
            onDismiss = { }
        )
    }
}

data class EditReviewDialogSettings(
    val minRating: Int = 1,
    val maxRating: Int = 10,
    val reviewTextMinSymbols: Int = 10,
    val reviewTextMaxSymbols: Int = 500,
    val title: String? = null
) {
    fun getDefaultRating(): Float = ((maxRating + minRating) / 2f).roundToInt().toFloat()
}

data class EditReviewDialogResult(
    val rating: Int,
    val reviewText: String
)