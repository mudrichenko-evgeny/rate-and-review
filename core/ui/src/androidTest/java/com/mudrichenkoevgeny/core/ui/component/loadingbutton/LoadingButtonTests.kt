package com.mudrichenkoevgeny.core.ui.component.loadingbutton

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.mudrichenkoevgeny.core.ui.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoadingButtonTests {

    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setupTests() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testLoadingButtonLoadingState() {
        composeTestRule.setContent {
            LoadingButton(
                state = LoadingButtonState.LOADING
            )
        }

        composeTestRule.onNodeWithTag(LOADING_BUTTON_CIRCULAR_PROGRESS_INDICATOR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testLoadingButtonSuccessfulState() {
        composeTestRule.setContent {
            LoadingButton(
                state = LoadingButtonState.SUCCESSFUL
            )
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_successful)).assertIsDisplayed()
    }

    @Test
    fun testLoadingButtonDefaultState() {
        val text = "LoadingButton"
        composeTestRule.setContent {
            LoadingButton(
                state = LoadingButtonState.ENABLED
            )
        }

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }
}