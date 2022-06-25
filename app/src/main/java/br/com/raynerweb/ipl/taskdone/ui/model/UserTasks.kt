package br.com.raynerweb.ipl.taskdone.ui.model

data class UserTasks(
    val user: User,
    val tasks: List<Task>
)