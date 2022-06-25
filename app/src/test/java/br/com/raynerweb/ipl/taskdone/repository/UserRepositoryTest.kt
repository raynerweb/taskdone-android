package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.repository.impl.UserRepositoryImpl
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.ui.model.User
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class UserRepositoryTest {

    private val userDao = mock<UserDao>()
    private val repository = UserRepositoryImpl(userDao)

    @Test
    fun `Should save user with success`() = runBlocking {
        repository.save(user())
        verify(userDao).save(any())
    }

    @Test
    fun `Should find user by email`() = runBlocking {
        whenever(userDao.findByEmail(any())).thenReturn(userEntity())
        val user = repository.findByEmail("email@email.com")
        assertNotNull(user)
    }

    @Test
    fun `Should return null when user not be found by email`() = runBlocking {
        whenever(userDao.findByEmail(any())).thenReturn(null)
        val user = repository.findByEmail("email@email.com")
        assertNull(user)
    }

    private fun userEntity() = UserEntity(
        userId = 1,
        email = "email@email.com",
        name = "Rayner",
    )

    private fun user() = User(
        email = "raynerweb@gmail.com",
        name = "Rayner"
    )

}