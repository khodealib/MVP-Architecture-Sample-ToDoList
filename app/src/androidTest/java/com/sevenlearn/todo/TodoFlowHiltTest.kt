package com.sevenlearn.todo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sevenlearn.todo.ui.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TodoFlowHiltTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun createToggleSearchEditDeleteAll_flow() {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("No tasks yet").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.ADD_TASK_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.TASK_TITLE_FIELD).performTextInput("Buy milk")
        composeRule.onNodeWithTag(TestTags.IMPORTANCE_HIGH).performClick()
        composeRule.onNodeWithTag(TestTags.SAVE_BUTTON).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Buy milk").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Buy milk").assertIsDisplayed()
        composeRule.onNodeWithText("Buy milk").performClick()

        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("milk")
        composeRule.onNodeWithText("Buy milk").assertIsDisplayed()

        composeRule.onNodeWithText("Buy milk").performTouchInput { longClick() }
        composeRule.onNodeWithTag(TestTags.TASK_TITLE_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.TASK_TITLE_FIELD).performTextClearance()
        composeRule.onNodeWithTag(TestTags.TASK_TITLE_FIELD).performTextInput("Buy oat milk")
        composeRule.onNodeWithTag(TestTags.SAVE_BUTTON).performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Buy oat milk").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Buy oat milk").assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.DELETE_ALL_BUTTON).performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("No tasks yet").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()
    }

    @Test
    fun appContext_packageName() {
        val context = composeRule.activity.applicationContext
        assert(context.packageName == "com.sevenlearn.todo")
    }
}
