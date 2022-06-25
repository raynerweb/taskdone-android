package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.ui.model.Task
import br.com.raynerweb.ipl.taskdone.ui.model.User


interface TaskRepository {

    suspend fun save(user: User, task: Task)

    suspend fun delete(task: Task)

}