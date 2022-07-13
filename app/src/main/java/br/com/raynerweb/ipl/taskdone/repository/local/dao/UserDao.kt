package br.com.raynerweb.ipl.taskdone.repository.local.dao

import androidx.room.*
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserWithTasks
import br.com.raynerweb.ipl.taskdone.ui.model.User

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

    @Transaction
    @Query("SELECT * FROM users WHERE users.isLocal == 1")
    fun findLocalUser(): UserEntity?

    @Update
    fun update(userEntity: UserEntity)

    @Insert
    fun save(userEntity: UserEntity)
}