package com.mudrichenkoevgeny.core.ui.component.expandabletext

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.theme.Theme

const val DEFAULT_MINIMUM_TEXT_LINE = 2

/**
 * An expandable text component that provides access to truncated text with a dynamic ... Show More/ Show Less button.
 *
 * @author Mun Bonecci: https://medium.com/@munbonecci/how-to-implement-expandable-text-in-jetpack-compose-ca9ba35b645c
 *
 * @param modifier Modifier for the Box containing the text.
 * @param textModifier Modifier for the Text composable.
 * @param style The TextStyle to apply to the text.
 * @param fontStyle The FontStyle to apply to the text.
 * @param text The text to be displayed.
 * @param collapsedMaxLine The maximum number of lines to display when collapsed.
 * @param showMoreText The text to display for "... Show More" button.
 * @param showMoreStyle The SpanStyle for "... Show More" button.
 * @param showLessText The text to display for "Show Less" button.
 * @param showLessStyle The SpanStyle for "Show Less" button.
 * @param textAlign The alignment of the text.
 */
@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    text: String,
    collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
    showMoreText: String = "... ${stringResource(R.string.expandable_text_show_more)}",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String = " ${stringResource(R.string.expandable_text_show_less)}",
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null
) {
    // State variables to track the expanded state, clickable state, and last character index.
    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    ExpandableTextUI(
        modifier = modifier,
        textModifier = textModifier,
        isExpanded = isExpanded,
        isClickable = isClickable,
        onClicked = { isExpanded = !isExpanded },
        text = text,
        showMoreText = showMoreText,
        showLessText = showLessText,
        collapsedMaxLine = collapsedMaxLine,
        lastCharIndex = lastCharIndex,
        onTextLayout = { charIndex ->
            isClickable = true
            lastCharIndex = charIndex
        }
    )
}

@Composable
private fun ExpandableTextUI(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    isExpanded: Boolean,
    isClickable: Boolean,
    onClicked: () -> Unit,
    text: String,
    showMoreText: String,
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String,
    showLessStyle: SpanStyle = showMoreStyle,
    collapsedMaxLine: Int,
    lastCharIndex: Int,
    onTextLayout: (Int) -> Unit
) {
    Box(modifier = Modifier
        .clickable(isClickable) {
            onClicked()
        }
        .then(modifier)
    ) {
        // Text composable with buildAnnotatedString to handle "Show More" and "Show Less" buttons.
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (isClickable) {
                    if (isExpanded) {
                        // Display the full text and "Show Less" button when expanded.
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        // Display truncated text and "Show More" button when collapsed.
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = showMoreStyle) { append(showMoreText) }
                    }
                } else {
                    // Display the full text when not clickable.
                    append(text)
                }
            },
            // Set max lines based on the expanded state.
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            // Callback to determine visual overflow and enable click ability.
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    onTextLayout(textLayoutResult.getLineEnd(collapsedMaxLine - 1))
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableTextDefaultPreview() {
    Theme {
        ExpandableTextUI(
            isExpanded = false,
            isClickable = false,
            onClicked = { },
            text = MockConstants.MOCK_TEXT_LARGE,
            showMoreText = "... Show More",
            showLessText = " Show Less",
            collapsedMaxLine = 2,
            lastCharIndex = 0,
            onTextLayout = { }
        )
    }
}