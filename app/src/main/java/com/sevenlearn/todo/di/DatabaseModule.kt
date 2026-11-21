package com.sevenlearn.todo.di

import android.content.Context
import androidx.room.Room
import com.sevenlearn.todo.data.AppDatabase
import com.sevenlearn.todo.data.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
}
