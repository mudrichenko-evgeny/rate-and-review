package com.mudrichenkoevgeny.core.ui.component.errortext

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.mudrichenkoevgeny.core.ui.R
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ErrorTextTests {

    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setupTests() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testErrorTextComponentsVisibility() {
        val text = "Unknown error"
        composeTestRule.setContent {
            ErrorTextWithIcon(
                text = text
            )
        }

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_error)).assertIsDisplayed()
    }

    @Test
    fun testErrorTextActionButton() {
        val text = "Unknown error"
        var isActionClicked = false
        composeTestRule.setContent {
            ErrorTextWithCloseButton(
                text = text,
                onCloseButtonClicked = { isActionClicked = true }
            )
        }

        val closeButton = composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_close))

        closeButton.assertIsDisplayed()
        closeButton.performClick()
        assertTrue(isActionClicked)
    }
}