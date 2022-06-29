package br.com.raynerweb.ipl.taskdone.di

import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.repository.impl.TaskRepositoryImpl
import br.com.raynerweb.ipl.taskdone.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}