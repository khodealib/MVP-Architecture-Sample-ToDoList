package com.sevenlearn.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    val importance: Int = IMPORTANCE_NORMAL,
) {
    companion object {
        const val IMPORTANCE_LOW = 0
        const val IMPORTANCE_NORMAL = 1
        const val IMPORTANCE_HIGH = 2
    }
}
