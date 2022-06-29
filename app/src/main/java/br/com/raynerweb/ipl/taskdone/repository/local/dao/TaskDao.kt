package br.com.raynerweb.ipl.taskdone.repository.local.dao

import androidx.room.*
import br.com.raynerweb.ipl.taskdone.repository.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks")
    fun findAll(): List<TaskEntity>

    @Insert
    fun save(taskEntity: TaskEntity): Long

    @Delete
    fun delete(songs: TaskEntity)
}