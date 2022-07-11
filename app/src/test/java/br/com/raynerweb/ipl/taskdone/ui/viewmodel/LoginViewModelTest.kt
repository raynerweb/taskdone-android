package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class LoginViewModelTest {

    private val taskRepository = mock<TaskRepository>()
    private val userRepository = mock<UserRepository>()

    @InjectMocks
    lateinit var viewModel: DashboardViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @CallSuper
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

//    Scenario 1 - Login para trabalhar com um time
//    Given that I am in the login screen
//    When I click on Work in Team
//    Then I must select my Google credentials to stay logged in and work in team
//    And I should be directed to the Dashboard screen

    //    Scenario 2 - Login para trabalhar sozinho - apresentar campos
//    Given that I am at the login screen
//    When I click on Work alone
//    Then I should see the email field
//    And see the name field
//    And the save button to save
    @Test
    fun `Scenario 2 - Login para trabalhar sozinho - apresentar campos`(): Unit =
        runBlocking {
        }

//    Scenario 3 - Login para trabalhar sozinho - Validação dos campos
//    Dado que eu tenha clicado em trabalhar sozinho
//    Quando eu nao preencher os campos de email e nome e clicar em salvar
//    Entao eu devo ver a seguinte mensagem para ambos os campos: "Required field" message

//    Scenario 4 - Login para trabalhar sozinho - realizar login
//    Dado que eu tenha clicado em trabalhar sozinho
//    Quando eu preencher corretamente os campos de email e nome
//    When I click Save
//    Then eu devo ser direcionado para ecrã Dashboard
}