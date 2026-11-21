package com.sevenlearn.todo.di

import com.sevenlearn.todo.data.DefaultTaskRepository
import com.sevenlearn.todo.data.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(repository: DefaultTaskRepository): TaskRepository
}
