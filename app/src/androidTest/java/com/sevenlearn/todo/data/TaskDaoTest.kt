package com.sevenlearn.todo.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetById() = runTest {
        val id = dao.insert(Task(title = "Laundry", importance = Task.IMPORTANCE_HIGH))
        val loaded = dao.getById(id)
        assertThat(loaded).isNotNull()
        assertThat(loaded!!.title).isEqualTo("Laundry")
        assertThat(loaded.importance).isEqualTo(Task.IMPORTANCE_HIGH)
    }

    @Test
    fun observeAll_ordersByIdDescending() = runTest {
        dao.insert(Task(title = "First"))
        dao.insert(Task(title = "Second"))

        dao.observeAll().test {
            val items = awaitItem()
            assertThat(items.map { it.title }).containsExactly("Second", "First").inOrder()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun search_filtersByTitle() = runTest {
        dao.insert(Task(title = "Buy milk"))
        dao.insert(Task(title = "Walk dog"))

        dao.search("milk").test {
            val items = awaitItem()
            assertThat(items).hasSize(1)
            assertThat(items.first().title).isEqualTo("Buy milk")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateAndDelete() = runTest {
        val id = dao.insert(Task(title = "Draft"))
        val task = dao.getById(id)!!
        assertThat(dao.update(task.copy(title = "Final", isCompleted = true))).isEqualTo(1)
        assertThat(dao.getById(id)!!.title).isEqualTo("Final")
        assertThat(dao.getById(id)!!.isCompleted).isTrue()

        assertThat(dao.delete(dao.getById(id)!!)).isEqualTo(1)
        assertThat(dao.getById(id)).isNull()
    }

    @Test
    fun deleteAll_removesEverything() = runTest {
        dao.insert(Task(title = "A"))
        dao.insert(Task(title = "B"))
        dao.deleteAll()

        dao.observeAll().test {
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
