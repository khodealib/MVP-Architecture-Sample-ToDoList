package com.sevenlearn.todo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TaskRepository {
    fun observeTasks(query: String): Flow<List<Task>>
    suspend fun getTask(id: Long): Task?
    suspend fun addTask(task: Task): Long
    suspend fun updateTask(task: Task): Int
    suspend fun deleteTask(task: Task): Int
    suspend fun deleteAll()
}

@Singleton
class DefaultTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
) : TaskRepository {
    override fun observeTasks(query: String): Flow<List<Task>> {
        val trimmed = query.trim()
        return if (trimmed.isEmpty()) {
            taskDao.observeAll()
        } else {
            taskDao.search(trimmed)
        }
    }

    override suspend fun getTask(id: Long): Task? = taskDao.getById(id)

    override suspend fun addTask(task: Task): Long = taskDao.insert(task)

    override suspend fun updateTask(task: Task): Int = taskDao.update(task)

    override suspend fun deleteTask(task: Task): Int = taskDao.delete(task)

    override suspend fun deleteAll() = taskDao.deleteAll()
}
