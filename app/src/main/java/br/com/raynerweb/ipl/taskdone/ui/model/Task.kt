package br.com.raynerweb.ipl.taskdone.ui.model

data class Task(
    val taskId: String,
    val description: String,
    val date: String,
    val status: Status
)