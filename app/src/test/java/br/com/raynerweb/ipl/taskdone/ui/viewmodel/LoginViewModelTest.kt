package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.ValidationType
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
    lateinit var viewModel: LoginViewModel

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


    /**
     * Scenario 3 - Login para trabalhar sozinho - Campos obrigatórios
     * Dado que eu queira trabalhar sozinho
     * Quando eu nao preencher os campos de email e nome
     * E clicar em salvar
     * Entao eu devo ver a seguinte mensagem para ambos os campos: "Required field"
     */
    @Test
    fun `Scenario 3 - Login para trabalhar sozinho - Campos obrigatórios`(): Unit =
        runBlocking {

            val emailValidationObserver = spy<Observer<ValidationType>>()
            viewModel.showEmailValidation.observeForever(emailValidationObserver)

            val nameValidationObserver = spy<Observer<ValidationType>>()
            viewModel.showNameValidation.observeForever(nameValidationObserver)

            viewModel.login()

            verify(emailValidationObserver).onChanged(eq(ValidationType.REQUIRED))
            verify(nameValidationObserver).onChanged(eq(ValidationType.REQUIRED))
        }

    /**
     * Scenario 4 - Login para trabalhar sozinho - login com sucesso
     * Dado que eu queira trabalhar sozinho
     * Quando eu preencher corretamente os campos de email e nome
     * E clicar em Salvar
     * Entao eu devo ser direcionado para ecra Dashboard
     */
    @Test
    fun `Scenario 4 - Login para trabalhar sozinho - login com sucesso`(): Unit =
        runBlocking {
            val loginSuccessObserver = spy<Observer<Unit>>()
            viewModel.loginSuccess.observeForever(loginSuccessObserver)

            viewModel.email.postValue("email@email.com")
            viewModel.name.postValue("name")

            viewModel.login()

            verify(loginSuccessObserver).onChanged(null)
        }
}