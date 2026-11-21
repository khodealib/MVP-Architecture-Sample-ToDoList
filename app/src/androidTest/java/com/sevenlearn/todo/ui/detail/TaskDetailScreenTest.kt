package com.sevenlearn.todo.ui.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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
class TaskDetailScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_showsIndicatorAndDisablesSave() {
        composeRule.setContent {
            TodoTheme {
                val snackbar = remember { SnackbarHostState() }
                TaskDetailScreen(
                    uiState = TaskDetailUiState(isLoading = true),
                    snackbarHostState = snackbar,
                    onTitleChange = {},
                    onImportanceChange = {},
                    onSave = {},
                    onDelete = {},
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithTag(TestTags.LOADING).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SAVE_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun createMode_hidesDeleteAndSavesTitle() {
        var saved = false
        var lastImportance = Task.IMPORTANCE_NORMAL
        var lastTitle = ""

        composeRule.setContent {
            TodoTheme {
                val snackbar = remember { SnackbarHostState() }
                var title by remember { mutableStateOf("") }
                var importance by remember { mutableIntStateOf(Task.IMPORTANCE_NORMAL) }

                TaskDetailScreen(
                    uiState = TaskDetailUiState(
                        title = title,
                        importance = importance,
                        isEditMode = false,
                        isLoading = false,
                    ),
                    snackbarHostState = snackbar,
                    onTitleChange = {
                        title = it
                        lastTitle = it
                    },
                    onImportanceChange = {
                        importance = it
                        lastImportance = it
                    },
                    onSave = { saved = true },
                    onDelete = {},
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("New Task").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.DELETE_TASK_BUTTON).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.IMPORTANCE_HIGH).performClick()
        composeRule.onNodeWithTag(TestTags.TASK_TITLE_FIELD).performTextInput("My task")
        composeRule.onNodeWithTag(TestTags.SAVE_BUTTON).assertIsEnabled().performClick()

        assertThat(lastImportance).isEqualTo(Task.IMPORTANCE_HIGH)
        assertThat(lastTitle).isEqualTo("My task")
        assertThat(saved).isTrue()
    }

    @Test
    fun editMode_showsDeleteButton() {
        var deleted = false
        composeRule.setContent {
            TodoTheme {
                val snackbar = remember { SnackbarHostState() }
                TaskDetailScreen(
                    uiState = TaskDetailUiState(
                        title = "Existing",
                        isEditMode = true,
                        isLoading = false,
                    ),
                    snackbarHostState = snackbar,
                    onTitleChange = {},
                    onImportanceChange = {},
                    onSave = {},
                    onDelete = { deleted = true },
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("Edit Task").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.DELETE_TASK_BUTTON).assertIsDisplayed().performClick()
        assertThat(deleted).isTrue()
    }
}
