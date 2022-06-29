package br.com.raynerweb.ipl.taskdone.di

import android.content.Context
import androidx.room.Room
import br.com.raynerweb.ipl.taskdone.repository.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "TaskDoneDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideTaskDao(database: AppDatabase) = database.taskDao()

    @Singleton
    @Provides
    fun provideUserTaskDao(database: AppDatabase) = database.userTaskDao()

}