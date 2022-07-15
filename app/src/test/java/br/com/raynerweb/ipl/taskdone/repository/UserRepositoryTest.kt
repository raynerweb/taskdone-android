package br.com.raynerweb.ipl.taskdone.repository

import br.com.raynerweb.ipl.taskdone.mocks.Mocks.LOCAL_USER
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER_ENTITY
import br.com.raynerweb.ipl.taskdone.repository.impl.UserRepositoryImpl
import br.com.raynerweb.ipl.taskdone.repository.local.dao.UserDao
import br.com.raynerweb.ipl.taskdone.repository.preference.LoginPreference
import br.com.raynerweb.ipl.taskdone.repository.preference.TeamPreference
import br.com.raynerweb.ipl.taskdone.ui.model.User
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class UserRepositoryTest {

    private val userDao = mock<UserDao>()
    private val loginPreference = mock<LoginPreference>()
    private val teamPreference = mock<TeamPreference>()
    private val repository = UserRepositoryImpl(userDao, loginPreference, teamPreference)

    @Test
    fun `Should save user with success`() = runBlocking {
        repository.save(LOCAL_USER)
        verify(userDao).save(any())
    }

    @Test
    fun `Should update local user`() = runBlocking {
        whenever(userDao.findLocalUser()).thenReturn(USER_ENTITY)
        val newUser = User(
            email = "RAYNERWEB@GMAIL.COM",
            name = "CARVALHO",
            isLocal = true
        )
        repository.save(newUser)
        verify(userDao).update(any())
        assertTrue(repository.findLocalUser()?.name == newUser.name)
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

    @Test
    fun `Should find a local user`() = runBlocking {
        whenever(userDao.findLocalUser()).thenReturn(USER_ENTITY)
        val user = repository.findLocalUser()
        assertNotNull(user)
    }

}