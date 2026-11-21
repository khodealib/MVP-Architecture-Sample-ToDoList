package com.sevenlearn.todo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
) {
    fun observeTasks(query: String): Flow<List<Task>> {
        val trimmed = query.trim()
        return if (trimmed.isEmpty()) {
            taskDao.observeAll()
        } else {
            taskDao.search(trimmed)
        }
    }

    suspend fun getTask(id: Long): Task? = taskDao.getById(id)

    suspend fun addTask(task: Task): Long = taskDao.insert(task)

    suspend fun updateTask(task: Task): Int = taskDao.update(task)

    suspend fun deleteTask(task: Task): Int = taskDao.delete(task)

    suspend fun deleteAll() = taskDao.deleteAll()
}
