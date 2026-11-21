package com.sevenlearn.todo.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {
    private lateinit var taskDao: FakeTaskDao
    private lateinit var repository: DefaultTaskRepository

    @Before
    fun setUp() {
        taskDao = FakeTaskDao()
        repository = DefaultTaskRepository(taskDao)
    }

    @Test
    fun observeTasks_emptyQuery_usesObserveAll() = runTest {
        val tasks = listOf(Task(id = 1, title = "A"))
        taskDao.seed(tasks)

        repository.observeTasks("").test {
            assertThat(awaitItem()).isEqualTo(tasks)
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(taskDao.observeAllCalls).isEqualTo(1)
        assertThat(taskDao.searchQueries).isEmpty()
    }

    @Test
    fun observeTasks_blankQuery_usesObserveAll() = runTest {
        repository.observeTasks("   ").test {
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(taskDao.observeAllCalls).isEqualTo(1)
    }

    @Test
    fun observeTasks_withQuery_trimsAndSearches() = runTest {
        val tasks = listOf(Task(id = 2, title = "Buy milk"))
        taskDao.searchResults["milk"] = tasks

        repository.observeTasks("  milk  ").test {
            assertThat(awaitItem()).isEqualTo(tasks)
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(taskDao.searchQueries).containsExactly("milk")
    }

    @Test
    fun crud_delegatesToDao() = runTest {
        val task = Task(id = 1, title = "Task")
        taskDao.getByIdResult = task
        taskDao.insertResult = 10L
        taskDao.updateResult = 1
        taskDao.deleteResult = 1

        assertThat(repository.getTask(1)).isEqualTo(task)
        assertThat(repository.addTask(task)).isEqualTo(10L)
        assertThat(repository.updateTask(task)).isEqualTo(1)
        assertThat(repository.deleteTask(task)).isEqualTo(1)

        repository.deleteAll()
        assertThat(taskDao.deleteAllCalls).isEqualTo(1)
        assertThat(taskDao.inserted).containsExactly(task)
        assertThat(taskDao.updated).containsExactly(task)
        assertThat(taskDao.deleted).containsExactly(task)
    }

    private class FakeTaskDao : TaskDao {
        private val tasks = MutableStateFlow<List<Task>>(emptyList())
        var observeAllCalls = 0
            private set
        val searchQueries = mutableListOf<String>()
        val searchResults = mutableMapOf<String, List<Task>>()
        var getByIdResult: Task? = null
        var insertResult = 0L
        var updateResult = 0
        var deleteResult = 0
        var deleteAllCalls = 0
            private set
        val inserted = mutableListOf<Task>()
        val updated = mutableListOf<Task>()
        val deleted = mutableListOf<Task>()

        fun seed(items: List<Task>) {
            tasks.value = items
        }

        override fun observeAll(): Flow<List<Task>> {
            observeAllCalls++
            return tasks
        }

        override fun search(query: String): Flow<List<Task>> {
            searchQueries += query
            return flowOf(searchResults[query].orEmpty())
        }

        override suspend fun getById(id: Long): Task? = getByIdResult

        override suspend fun insert(task: Task): Long {
            inserted += task
            return insertResult
        }

        override suspend fun update(task: Task): Int {
            updated += task
            return updateResult
        }

        override suspend fun delete(task: Task): Int {
            deleted += task
            return deleteResult
        }

        override suspend fun deleteAll() {
            deleteAllCalls++
        }
    }
}
