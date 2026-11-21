package com.sevenlearn.todo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tbl_tasks ORDER BY id DESC")
    fun observeAll(): Flow<List<Task>>

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE '%' || :query || '%' ORDER BY id DESC")
    fun search(query: String): Flow<List<Task>>

    @Query("SELECT * FROM tbl_tasks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Task?

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task): Int

    @Delete
    suspend fun delete(task: Task): Int

    @Query("DELETE FROM tbl_tasks")
    suspend fun deleteAll()
}
