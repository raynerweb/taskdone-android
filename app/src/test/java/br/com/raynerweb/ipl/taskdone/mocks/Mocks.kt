package br.com.raynerweb.ipl.taskdone.mocks

import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import br.com.raynerweb.ipl.taskdone.ui.model.User
import br.com.raynerweb.ipl.taskdone.ui.model.UserTasks
import java.text.SimpleDateFormat
import java.util.*

object Mocks {

    val USER = User(
        email = "raynerweb@gmail.com",
        name = "Rayner"
    )
    val TASK = Task(
        taskId = "1",
        description = "Description",
        date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
        status = Status.BACKLOG
    )
    val USER_ENTITY = UserEntity(
        userId = 1,
        email = "email@email.com",
        name = "Rayner",
    )
    val USER_TASK = UserTasks(
        user = USER, tasks = listOf(TASK)
    )
    val USER_EMPTY_TASK = UserTasks(
        user = USER, tasks = emptyList()
    )
}