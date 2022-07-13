package br.com.raynerweb.ipl.taskdone.ui.model

data class User(
    val email: String,
    val name: String,
    val isLocal: Boolean,
    val photo: String? = null
)