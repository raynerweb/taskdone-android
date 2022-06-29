package br.com.raynerweb.ipl.taskdone.repository.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.raynerweb.ipl.taskdone.repository.local.AppDatabase
import br.com.raynerweb.ipl.taskdone.repository.local.entity.TaskEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        taskDao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun shouldSaveATask() = runBlocking {
        val task = TaskEntity(
            description = "Description",
            date = Date(),
            status = "BACKLOG"
        )
        taskDao.save(task)
        Assert.assertTrue(taskDao.findAll().isNotEmpty())
    }

    @Test
    fun shouldDeleteATask() = runBlocking {
        val task = TaskEntity(
            description = "Description",
            date = Date(),
            status = "BACKLOG"
        )
        taskDao.save(task)

        val tasks = taskDao.findAll()
        Assert.assertTrue(taskDao.findAll().isNotEmpty())

        tasks.forEach {
            taskDao.delete(it)
        }

        Assert.assertTrue(taskDao.findAll().isEmpty())

    }

}
