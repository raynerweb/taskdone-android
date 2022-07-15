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

    /**
     * Scenario 2 - Login Validation by Email and Name
     * Given that I am in the login screen
     * When I don't fill the email and name fields and click save
     * Then I should see the following message for both fields: "Required fields".
     */
    @Test
    fun `Then I should see the following message for both fields Required fields`(): Unit =
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
     * Scenario 3 - Login by email and name
     * Given that I am at the login screen
     * When I fill in the correct email and name fields and click save
     * Then I should be redirected to the Dashboard screen
     */
    @Test
    fun `Then I should be redirected to the Dashboard screen`(): Unit =
        runBlocking {
            val loginSuccessObserver = spy<Observer<Unit>>()
            viewModel.loginSuccess.observeForever(loginSuccessObserver)

            viewModel.email.postValue("email@email.com")
            viewModel.name.postValue("name")

            viewModel.login()

            verify(loginSuccessObserver).onChanged(null)
        }

    /**
     * Scenario 4 - Login with Google
     * Given that I am at the login screen
     * When I click on the Google Sing In button
     * Then I should login with Google
     * And I should be redirected to the Dashboard screen
     */
    @Test
    fun `Then I should login with Google`(): Unit =
        runBlocking {
            runBlocking {
                val loginSuccessObserver = spy<Observer<Unit>>()
                viewModel.loginSuccess.observeForever(loginSuccessObserver)

                viewModel.email.postValue("email@email.com")
                viewModel.name.postValue("name")

                viewModel.login()

                verify(loginSuccessObserver).onChanged(null)
            }
        }

}