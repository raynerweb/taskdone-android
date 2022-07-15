package br.com.raynerweb.ipl.taskdone.repository.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.raynerweb.ipl.taskdone.repository.local.AppDatabase
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun shouldSaveALocalUser() = runBlocking {
        val user = UserEntity(
            name = "Ráyner",
            email = "email@email.com",
            isLocal = true
        )
        userDao.save(user)
        Assert.assertTrue(userDao.findLocalUser()?.isLocal ?: false)
    }

    @Test
    fun shouldSaveAExternalUser() = runBlocking {
        val user = UserEntity(
            name = "Ráyner",
            email = "email@email.com",
            isLocal = false
        )
        userDao.save(user)
        Assert.assertTrue(userDao.findAll().isNotEmpty())
    }

    @Test
    fun shouldndFindAExternalUser() = runBlocking {
        val user = UserEntity(
            name = "Ráyner",
            email = "email@email.com",
            isLocal = false
        )
        userDao.save(user)
        Assert.assertNull(userDao.findLocalUser())
    }

}