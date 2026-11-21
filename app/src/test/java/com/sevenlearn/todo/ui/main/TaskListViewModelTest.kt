package com.sevenlearn.todo.ui.main

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sevenlearn.todo.data.FakeTaskRepository
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTaskRepository
    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setUp() {
        repository = FakeTaskRepository()
        viewModel = TaskListViewModel(repository)
    }

    @Test
    fun uiState_initiallyEmpty_thenShowsSeededTasks() = runTest {
        repository.seed(Task(id = 1, title = "First"))

        viewModel.uiState.test {
            assertThat(awaitItem().isEmpty).isTrue()
            advanceTimeBy(250)
            advanceUntilIdle()
            val state = awaitItem()
            assertThat(state.tasks).hasSize(1)
            assertThat(state.isEmpty).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun search_debouncesAndFilters() = runTest {
        repository.seed(
            Task(id = 1, title = "Buy milk"),
            Task(id = 2, title = "Walk dog"),
        )

        viewModel.uiState.test {
            skipItems(1)
            advanceTimeBy(250)
            advanceUntilIdle()
            skipItems(1)

            viewModel.onSearchQueryChange("milk")
            advanceTimeBy(249)
            expectNoEvents()
            advanceTimeBy(1)
            advanceUntilIdle()

            val filtered = awaitItem()
            assertThat(filtered.searchQuery).isEqualTo("milk")
            assertThat(filtered.tasks).hasSize(1)
            assertThat(filtered.tasks.first().title).isEqualTo("Buy milk")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun toggleCompleted_updatesRepository() = runTest {
        val task = Task(id = 1, title = "Task", isCompleted = false)
        repository.seed(task)

        viewModel.toggleCompleted(task)
        advanceUntilIdle()

        assertThat(repository.updatedTasks).hasSize(1)
        assertThat(repository.updatedTasks.first().isCompleted).isTrue()
    }

    @Test
    fun deleteAll_clearsRepository() = runTest {
        repository.seed(Task(id = 1, title = "Task"))

        viewModel.deleteAll()
        advanceUntilIdle()

        assertThat(repository.deleteAllCalls).isEqualTo(1)
    }
}
