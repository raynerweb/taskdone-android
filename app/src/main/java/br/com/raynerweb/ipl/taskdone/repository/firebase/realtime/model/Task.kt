package br.com.raynerweb.ipl.taskdone.repository.firebase.realtime.model

import java.util.*

data class Task(
    val description: String,
    val date: Date,
    val status: String,
    val creationDate: Date,
)