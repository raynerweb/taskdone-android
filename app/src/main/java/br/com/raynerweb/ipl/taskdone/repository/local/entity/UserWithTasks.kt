package br.com.raynerweb.ipl.taskdone.repository.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithTasks(
    @Embedded
    val userEntity: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "taskId",
        associateBy = Junction(UserTaskEntity::class)
    )
    val tasks: List<TaskEntity>
)