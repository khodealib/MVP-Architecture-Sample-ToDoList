package com.sevenlearn.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "db_app"
    }
}
