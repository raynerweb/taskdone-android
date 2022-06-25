package br.com.raynerweb.ipl.taskdone.repository.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "taskId"])
data class UserTaskEntity(
    val userId: Long,
    val taskId: Long,
)