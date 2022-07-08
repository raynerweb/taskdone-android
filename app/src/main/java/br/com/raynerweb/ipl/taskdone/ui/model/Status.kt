package br.com.raynerweb.ipl.taskdone.ui.model

enum class Status {
    TODO,
    IN_PROGRESS,
    DONE;

    companion object {
        fun getStatus(statusName: String): Status {
            return values().find { status: Status -> status.name == statusName } ?: TODO
        }
    }
}