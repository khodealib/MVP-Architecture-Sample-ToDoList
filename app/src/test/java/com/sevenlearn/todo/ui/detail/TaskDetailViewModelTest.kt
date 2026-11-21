package com.sevenlearn.todo.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sevenlearn.todo.R
import com.sevenlearn.todo.data.FakeTaskRepository
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.navigation.NavRoutes
import com.sevenlearn.todo.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        repository: FakeTaskRepository,
        taskId: Long? = null,
    ): TaskDetailViewModel {
        val handle = if (taskId == null) {
            SavedStateHandle()
        } else {
            SavedStateHandle(mapOf(NavRoutes.TASK_ID_ARG to taskId))
        }
        return TaskDetailViewModel(repository, handle)
    }

    @Test
    fun createMode_whenNoTaskId() = runTest {
        val repository = FakeTaskRepository()
        val viewModel = createViewModel(repository, taskId = NavRoutes.NO_TASK_ID)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isEditMode).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.title).isEmpty()
    }

    @Test
    fun editMode_loadsExistingTask() = runTest {
        val repository = FakeTaskRepository()
        repository.seed(Task(id = 5, title = "Existing", importance = Task.IMPORTANCE_HIGH))
        val viewModel = createViewModel(repository, taskId = 5)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isEditMode).isTrue()
        assertThat(state.title).isEqualTo("Existing")
        assertThat(state.importance).isEqualTo(Task.IMPORTANCE_HIGH)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun editMode_missingTask_emitsNotFoundError() = runTest {
        val repository = FakeTaskRepository()
        val viewModel = createViewModel(repository, taskId = 99)

        viewModel.events.test {
            advanceUntilIdle()
            val event = awaitItem()
            assertThat(event).isEqualTo(TaskDetailEvent.Error(R.string.error_task_not_found))
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun save_emptyTitle_emitsError() = runTest {
        val repository = FakeTaskRepository()
        val viewModel = createViewModel(repository, taskId = NavRoutes.NO_TASK_ID)
        advanceUntilIdle()

        viewModel.events.test {
            viewModel.save()
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(TaskDetailEvent.Error(R.string.error_enter_task_title))
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(repository.addedTasks).isEmpty()
    }

    @Test
    fun save_createMode_insertsTask() = runTest {
        val repository = FakeTaskRepository()
        val viewModel = createViewModel(repository, taskId = NavRoutes.NO_TASK_ID)
        advanceUntilIdle()

        viewModel.onTitleChange(" New task ")
        viewModel.onImportanceChange(Task.IMPORTANCE_LOW)

        viewModel.events.test {
            viewModel.save()
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(TaskDetailEvent.Saved)
            cancelAndIgnoreRemainingEvents()
        }

        assertThat(repository.addedTasks).hasSize(1)
        assertThat(repository.addedTasks.first().title).isEqualTo("New task")
        assertThat(repository.addedTasks.first().importance).isEqualTo(Task.IMPORTANCE_LOW)
    }

    @Test
    fun save_editMode_updatesTask() = runTest {
        val repository = FakeTaskRepository()
        repository.seed(Task(id = 3, title = "Old", importance = Task.IMPORTANCE_NORMAL))
        val viewModel = createViewModel(repository, taskId = 3)
        advanceUntilIdle()

        viewModel.onTitleChange("Updated")
        viewModel.onImportanceChange(Task.IMPORTANCE_HIGH)

        viewModel.events.test {
            viewModel.save()
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(TaskDetailEvent.Saved)
            cancelAndIgnoreRemainingEvents()
        }

        assertThat(repository.updatedTasks).hasSize(1)
        assertThat(repository.updatedTasks.first().title).isEqualTo("Updated")
        assertThat(repository.updatedTasks.first().importance).isEqualTo(Task.IMPORTANCE_HIGH)
    }

    @Test
    fun delete_emitsDeleted() = runTest {
        val repository = FakeTaskRepository()
        val task = Task(id = 7, title = "Remove me")
        repository.seed(task)
        val viewModel = createViewModel(repository, taskId = 7)
        advanceUntilIdle()

        viewModel.events.test {
            viewModel.delete()
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(TaskDetailEvent.Deleted)
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(repository.deletedTasks).containsExactly(task)
    }
}
