package com.sevenlearn.todo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTaskRepository : TaskRepository {
    private val tasks = MutableStateFlow<List<Task>>(emptyList())
    private var nextId = 1L

    val updatedTasks = mutableListOf<Task>()
    val deletedTasks = mutableListOf<Task>()
    val addedTasks = mutableListOf<Task>()
    var deleteAllCalls = 0
        private set

    fun seed(vararg items: Task) {
        tasks.value = items.toList()
        nextId = (items.maxOfOrNull { it.id } ?: 0L) + 1
    }

    override fun observeTasks(query: String): Flow<List<Task>> {
        val trimmed = query.trim()
        return tasks.map { list ->
            if (trimmed.isEmpty()) {
                list.sortedByDescending { it.id }
            } else {
                list.filter { it.title.contains(trimmed, ignoreCase = true) }
                    .sortedByDescending { it.id }
            }
        }
    }

    override suspend fun getTask(id: Long): Task? = tasks.value.firstOrNull { it.id == id }

    override suspend fun addTask(task: Task): Long {
        val id = if (task.id == 0L) nextId++ else task.id
        val saved = task.copy(id = id)
        addedTasks += saved
        tasks.update { it + saved }
        return id
    }

    override suspend fun updateTask(task: Task): Int {
        updatedTasks += task
        var changed = 0
        tasks.update { current ->
            current.map {
                if (it.id == task.id) {
                    changed = 1
                    task
                } else {
                    it
                }
            }
        }
        return changed
    }

    override suspend fun deleteTask(task: Task): Int {
        deletedTasks += task
        var changed = 0
        tasks.update { current ->
            val filtered = current.filterNot { it.id == task.id }
            if (filtered.size != current.size) changed = 1
            filtered
        }
        return changed
    }

    override suspend fun deleteAll() {
        deleteAllCalls++
        tasks.value = emptyList()
    }
}
