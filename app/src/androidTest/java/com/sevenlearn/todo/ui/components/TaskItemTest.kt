package com.sevenlearn.todo.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.ui.TestTags
import com.sevenlearn.todo.ui.theme.TodoTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskItemTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickAndLongClick_invokeCallbacks() {
        var clicked = false
        var longClicked = false
        val task = Task(id = 9, title = "Call mom", isCompleted = true)

        composeRule.setContent {
            TodoTheme {
                TaskItem(
                    task = task,
                    onClick = { clicked = true },
                    onLongClick = { longClicked = true },
                )
            }
        }

        composeRule.onNodeWithText("Call mom").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.taskItem(9)).performClick()
        assertThat(clicked).isTrue()

        composeRule.onNodeWithTag(TestTags.taskItem(9)).performTouchInput { longClick() }
        assertThat(longClicked).isTrue()
    }
}
