package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import br.com.raynerweb.ipl.taskdone.ui.model.User
import java.util.*


interface TaskRepository {

    suspend fun save(user: User, description: String, date: Date, status: Status)

    suspend fun delete(task: Task)

}