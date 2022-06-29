package br.com.raynerweb.ipl.taskdone.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,
    val remoteId: Long = 0,
    val description: String,
    val date: Date,
    val status: String
)
