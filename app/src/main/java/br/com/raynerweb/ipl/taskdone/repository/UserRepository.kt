package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.ui.model.User
import br.com.raynerweb.ipl.taskdone.ui.model.UserTasks

interface UserRepository {

    suspend fun save(user: User)

    suspend fun findByEmail(email: String): User?

    suspend fun findAll(): List<UserTasks>

}