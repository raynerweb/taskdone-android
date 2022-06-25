package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER_ENTITY
import br.com.raynerweb.ipl.taskdone.repository.impl.UserRepositoryImpl
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
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
        repository.save(USER)
        verify(userDao).save(any())
    }

    @Test
    fun `Should find user by email`() = runBlocking {
        whenever(userDao.findByEmail(any())).thenReturn(USER_ENTITY)
        val user = repository.findByEmail("email@email.com")
        assertNotNull(user)
    }

    @Test
    fun `Should return null when user not be found by email`() = runBlocking {
        whenever(userDao.findByEmail(any())).thenReturn(null)
        val user = repository.findByEmail("email@email.com")
        assertNull(user)
    }

}