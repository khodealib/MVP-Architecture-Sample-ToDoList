package com.sevenlearn.todo.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {
    private lateinit var taskDao: TaskDao
    private lateinit var repository: DefaultTaskRepository

    @Before
    fun setUp() {
        taskDao = mockk(relaxed = true)
        repository = DefaultTaskRepository(taskDao)
    }

    @Test
    fun observeTasks_emptyQuery_usesObserveAll() = runTest {
        val tasks = listOf(Task(id = 1, title = "A"))
        every { taskDao.observeAll() } returns flowOf(tasks)

        repository.observeTasks("").test {
            assertThat(awaitItem()).isEqualTo(tasks)
            awaitComplete()
        }
        verify(exactly = 1) { taskDao.observeAll() }
        verify(exactly = 0) { taskDao.search(any()) }
    }

    @Test
    fun observeTasks_blankQuery_usesObserveAll() = runTest {
        every { taskDao.observeAll() } returns flowOf(emptyList())

        repository.observeTasks("   ").test {
            assertThat(awaitItem()).isEmpty()
            awaitComplete()
        }
        verify { taskDao.observeAll() }
    }

    @Test
    fun observeTasks_withQuery_trimsAndSearches() = runTest {
        val tasks = listOf(Task(id = 2, title = "Buy milk"))
        every { taskDao.search("milk") } returns flowOf(tasks)

        repository.observeTasks("  milk  ").test {
            assertThat(awaitItem()).isEqualTo(tasks)
            awaitComplete()
        }
        verify { taskDao.search("milk") }
    }

    @Test
    fun crud_delegatesToDao() = runTest {
        val task = Task(id = 1, title = "Task")
        coEvery { taskDao.getById(1) } returns task
        coEvery { taskDao.insert(task) } returns 10L
        coEvery { taskDao.update(task) } returns 1
        coEvery { taskDao.delete(task) } returns 1

        assertThat(repository.getTask(1)).isEqualTo(task)
        assertThat(repository.addTask(task)).isEqualTo(10L)
        assertThat(repository.updateTask(task)).isEqualTo(1)
        assertThat(repository.deleteTask(task)).isEqualTo(1)

        repository.deleteAll()
        coVerify { taskDao.deleteAll() }
    }
}
