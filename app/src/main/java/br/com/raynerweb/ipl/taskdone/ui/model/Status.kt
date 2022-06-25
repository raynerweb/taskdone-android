package br.com.raynerweb.ipl.taskdone.ui.model

enum class Status {
    BACKLOG,
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun getStatus(statusName: String): Status {
            return values().find { status: Status -> status.name == statusName } ?: BACKLOG
        }
    }
}