package br.com.raynerweb.ipl.taskdone.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserWithTasks

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM users")
    fun findAll(): List<UserEntity>

    @Transaction
    @Query("SELECT * FROM users WHERE email = :email")
    fun findByEmail(email: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM users")
    fun findGroupedByUser(): List<UserWithTasks>

    @Insert
    fun save(userEntity: UserEntity)
}