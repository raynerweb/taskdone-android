package br.com.raynerweb.ipl.taskdone.repository.impl

import br.com.raynerweb.ipl.taskdone.ext.toTaskEntity
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.local.dao.TaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserTaskDao
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserTaskEntity
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import br.com.raynerweb.ipl.taskdone.ui.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val taskDao: TaskDao,
    private val userTaskDao: UserTaskDao
) : TaskRepository {

    override suspend fun save(user: User, task: Task): Unit =
        withContext(context = Dispatchers.IO) {
            val taskId = taskDao.save(task.toTaskEntity())
            userDao.findByEmail(user.email)?.let { userEntity ->
                userTaskDao.save(
                    mutableListOf(
                        UserTaskEntity(
                            userId = userEntity.userId,
                            taskId = taskId
                        )
                    )
                )
            }
        }

    override suspend fun delete(task: Task) {
        withContext(context = Dispatchers.IO) {
            taskDao.delete(task.toTaskEntity())
        }
    }

}