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
        status = Status.TODO
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

    val MOCK_FILTERS by lazy {
        UserTasks(
            user = USER, tasks = MOCK_TASKS
        )
    }

    val MOCK_TASKS = listOf(
        Task(
            taskId = "1",
            description = "ar",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.TODO
        ),
        Task(
            taskId = "2",
            description = "arara",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.IN_PROGRESS
        ),
        Task(
            taskId = "3",
            description = "araras",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.IN_PROGRESS
        ),
        Task(
            taskId = "4",
            description = "bar",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.TODO
        ),
        Task(
            taskId = "5",
            description = "bares",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.IN_PROGRESS
        ),
        Task(
            taskId = "6",
            description = "resma",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.IN_PROGRESS
        ),
        Task(
            taskId = "7",
            description = "ca",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.TODO
        ),
        Task(
            taskId = "8",
            description = "casa",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.IN_PROGRESS
        ),
        Task(
            taskId = "9",
            description = "casamento",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.DONE
        ),
        Task(
            taskId = "10",
            description = "asa",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.TODO
        ),
        Task(
            taskId = "11",
            description = "bazar",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.IN_PROGRESS
        ),
        Task(
            taskId = "12",
            description = "vazar",
            date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            status = Status.DONE
        )
    )
}