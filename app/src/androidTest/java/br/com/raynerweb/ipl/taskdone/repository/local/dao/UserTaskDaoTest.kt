package br.com.raynerweb.ipl.taskdone.repository.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.raynerweb.ipl.taskdone.repository.AppDatabase
import br.com.raynerweb.ipl.taskdone.repository.local.entity.TaskEntity
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserTaskEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserTaskDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var taskDao: TaskDao
    private lateinit var userTaskDao: UserTaskDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
        taskDao = db.taskDao()
        userTaskDao = db.userTaskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun shouldSaveAnUserWithTask() = runBlocking {
        val user = UserEntity(
            name = "Ráyner",
            email = "email@email.com",
        )
        userDao.save(user)
        Assert.assertTrue(userDao.findAll().isNotEmpty())

        val savedUser = userDao.findByEmail("email@email.com") ?: return@runBlocking
        junit.framework.Assert.assertEquals("email@email.com", user.email)

        val task = TaskEntity(
            description = "Description",
            date = Date(),
            status = "BACKLOG"
        )
        taskDao.save(task)

        val task2 = TaskEntity(
            description = "Another Task",
            date = Date(),
            status = "COMPLETED"
        )
        taskDao.save(task2)

        val savedTasks = taskDao.findAll()
        Assert.assertTrue(savedTasks.isNotEmpty())

        userTaskDao.save(savedTasks.map { savedTask ->
            UserTaskEntity(
                userId = savedUser.userId,
                taskId = savedTask.taskId
            )
        })

        Assert.assertTrue(userDao.findGroupedByUser().isNotEmpty())
        val grouped = userDao.findGroupedByUser()
        grouped.forEach { userWithTasks ->
            Assert.assertEquals(userWithTasks.userEntity.userId, savedUser.userId)
            Assert.assertEquals(savedTasks.size, userWithTasks.tasks.size)
        }
    }

}