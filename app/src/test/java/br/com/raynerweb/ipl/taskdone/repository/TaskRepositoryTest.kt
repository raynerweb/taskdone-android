package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.repository.impl.TaskRepositoryImpl
import br.com.raynerweb.ipl.taskdone.repository.local.dao.TaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserTaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import br.com.raynerweb.ipl.taskdone.ui.model.User
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class TaskRepositoryTest {

    private val userDao = mock<UserDao>()
    private val taskDao = mock<TaskDao>()
    private val userTaskDao = mock<UserTaskDao>()
    private val repository = TaskRepositoryImpl(userDao, taskDao, userTaskDao)

    @Test
    fun `Should save task with success`() = runBlocking {
        whenever(userDao.findByEmail(any())).thenReturn(userEntity())
        repository.save(user(), "Description", Date(), Status.BACKLOG)

        verify(taskDao).save(any())
        verify(userTaskDao).save(any())
    }

    @Test
    fun `Should delete task with success`() = runBlocking {
        repository.delete(task())

        verify(taskDao).delete(any())
    }

    private fun user() = User(
        email = "raynerweb@gmail.com",
        name = "Rayner"
    )

    private fun task() = Task(
        taskId = "1",
        description = "Description",
        date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
        status = Status.BACKLOG
    )

    private fun userEntity() = UserEntity(
        userId = 1,
        email = "email@email.com",
        name = "Rayner",
    )

}