package br.com.raynerweb.ipl.taskdone.repository.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(
            value = ["email"],
            unique = true
        )
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val remoteId: Long = 0,
    val name: String,
    val email: String,
)
