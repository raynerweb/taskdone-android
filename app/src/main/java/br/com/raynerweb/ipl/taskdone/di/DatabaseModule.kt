package br.com.raynerweb.ipl.taskdone.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        val from001To002 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Users ADD COLUMN photo TEXT")
                database.execSQL("ALTER TABLE Users ADD COLUMN isLocal INTEGER NOT NULL DEFAULT 1")
            }
        }

        return Room.databaseBuilder(context, AppDatabase::class.java, "TaskDoneDB")
            .addMigrations(from001To002)
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