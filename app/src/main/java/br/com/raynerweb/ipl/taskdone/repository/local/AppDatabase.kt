package br.com.raynerweb.ipl.taskdone.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.raynerweb.ipl.taskdone.repository.local.converter.BooleanConverter
import br.com.raynerweb.ipl.taskdone.repository.local.converter.DateConverter
import br.com.raynerweb.ipl.taskdone.repository.local.dao.TaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserTaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.entity.TaskEntity
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserTaskEntity

@Database(
    entities = [UserEntity::class, TaskEntity::class, UserTaskEntity::class],
    version = 1
)
@TypeConverters(DateConverter::class, BooleanConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun userTaskDao(): UserTaskDao

}