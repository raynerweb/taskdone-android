package br.com.raynerweb.ipl.taskdone.repository.impl

import br.com.raynerweb.ipl.taskdone.ext.toUser
import br.com.raynerweb.ipl.taskdone.ext.toUserEntity
import br.com.raynerweb.ipl.taskdone.ext.toUserTask
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.ui.model.User
import br.com.raynerweb.ipl.taskdone.ui.model.UserTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun save(user: User) =
        withContext(context = Dispatchers.IO) {
            userDao.save(user.toUserEntity())
        }

    override suspend fun findByEmail(email: String): User? {
        return withContext(context = Dispatchers.IO) {
            return@withContext userDao.findByEmail(email)?.let {
                return@let it.toUser()
            } ?: run {
                return@run null
            }
        }
    }

    override suspend fun findAll(): List<UserTasks> {
        return withContext(context = Dispatchers.IO) {
            return@withContext userDao.findGroupedByUser()
                .map { userWithTasks -> userWithTasks.toUserTask() }
        }
    }

}