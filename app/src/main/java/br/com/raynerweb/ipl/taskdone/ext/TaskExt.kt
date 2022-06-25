package br.com.raynerweb.ipl.taskdone.ext

import br.com.raynerweb.ipl.taskdone.repository.local.entity.TaskEntity
import br.com.raynerweb.ipl.taskdone.ui.model.Task

fun Task.toTaskEntity() = TaskEntity(
    taskId = this.taskId.toLong(),
    description = this.description,
    date = this.date.toDate(),
    status = this.status.name
)