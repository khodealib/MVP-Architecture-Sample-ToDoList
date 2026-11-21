package com.sevenlearn.todo.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.ui.TestTags
import com.sevenlearn.todo.ui.theme.TodoTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyState_isVisibleWhenNoTasks() {
        composeRule.setContent {
            TodoTheme {
                TaskListScreen(
                    uiState = TaskListUiState(isEmpty = true),
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onToggleCompleted = {},
                    onDeleteAll = {},
                    onAddTask = {},
                    onEditTask = {},
                )
            }
        }

        composeRule.onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()
        composeRule.onNodeWithText("No tasks yet").assertIsDisplayed()
    }

    @Test
    fun list_showsTasksAndInvokesCallbacks() {
        var addClicked = false
        var deleteAllClicked = false
        var toggled: Task? = null
        var lastSearch = ""
        val task = Task(id = 11, title = "Pay bills")

        composeRule.setContent {
            TodoTheme {
                var search by remember { mutableStateOf("") }
                TaskListScreen(
                    uiState = TaskListUiState(tasks = listOf(task), isEmpty = false),
                    searchQuery = search,
                    onSearchQueryChange = {
                        search = it
                        lastSearch = it
                    },
                    onToggleCompleted = { toggled = it },
                    onDeleteAll = { deleteAllClicked = true },
                    onAddTask = { addClicked = true },
                    onEditTask = {},
                )
            }
        }

        composeRule.onNodeWithTag(TestTags.TASK_LIST).assertIsDisplayed()
        composeRule.onNodeWithText("Pay bills").assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.ADD_TASK_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.DELETE_ALL_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.taskItem(11)).performClick()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("pay")

        assertThat(addClicked).isTrue()
        assertThat(deleteAllClicked).isTrue()
        assertThat(toggled).isEqualTo(task)
        assertThat(lastSearch).isEqualTo("pay")
    }
}
