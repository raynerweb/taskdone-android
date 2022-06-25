package br.com.raynerweb.ipl.taskdone.ext

import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserWithTasks
import br.com.raynerweb.ipl.taskdone.ui.model.UserTasks

fun UserWithTasks.toUserTask() = UserTasks(
    user = this.userEntity.toUser(),
    tasks = this.tasks.map { taskEntity -> taskEntity.toTask() }
)