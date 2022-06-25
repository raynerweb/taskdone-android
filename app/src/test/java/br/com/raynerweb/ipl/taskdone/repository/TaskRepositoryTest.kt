package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.mocks.Mocks.TASK
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER_ENTITY
import br.com.raynerweb.ipl.taskdone.repository.impl.TaskRepositoryImpl
import br.com.raynerweb.ipl.taskdone.repository.local.dao.TaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserTaskDao
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class TaskRepositoryTest {

    private val userDao = mock<UserDao>()
    private val taskDao = mock<TaskDao>()
    private val userTaskDao = mock<UserTaskDao>()
    private val repository = TaskRepositoryImpl(userDao, taskDao, userTaskDao)

    @Test
    fun `Should save task with success`() = runBlocking {
        whenever(userDao.findByEmail(any())).thenReturn(USER_ENTITY)
        repository.save(USER, "Description", Date(), Status.BACKLOG)

        verify(taskDao).save(any())
        verify(userTaskDao).save(any())
    }

    @Test
    fun `Should delete task with success`() = runBlocking {
        repository.delete(TASK)

        verify(taskDao).delete(any())
    }

}