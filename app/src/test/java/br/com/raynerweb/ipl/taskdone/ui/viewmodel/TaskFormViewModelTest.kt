package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class TaskFormViewModelTest {

    private val trainerRepository = mock<TaskRepository>()
    private val pokemonRepository = mock<UserRepository>()

    @InjectMocks
    lateinit var viewModel: TaskFormViewModel

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
     * Scenario 1 - Add a new Task
     * Given the user is on the management of tasks page
     * When the user clicks on the SAVE button without fill the fields
     * Then description field needs to show the message "Required Field"
     * And date field needs to show the message "Required Field"
     */
    @Test
    fun `Then description field needs to show the message Required Field`(): Unit =
        runBlocking {

            val observer = spy<Observer<Boolean>>()
            viewModel.showRequiredDescriptionMessage.observeForever(observer)

            viewModel.save()
            verify(observer).onChanged(eq(true))

        }

    /**
     * Scenario 1 - Add a new Task
     * Given the user is on the management of tasks page
     * When the user clicks on the SAVE button without fill the fields
     * Then description field needs to show the message "Required Field"
     * And date field needs to show the message "Required Field"
     */
    @Test
    fun `And date field needs to show the message Required Field`(): Unit =
        runBlocking {

            val observer = spy<Observer<Boolean>>()
            viewModel.showRequiredDateMessage.observeForever(observer)

            viewModel.save()
            verify(observer).onChanged(eq(true))

        }

    /**
     * Scenario 2 - Add a new Task
     * Given the user is on the management of task page
     * When the user filled the description field
     * And the user filled the date field
     * And click on the SAVE button
     * Then the user needs to see a saved task
     */
    @Test
    fun `Then the user needs to see a saved task`(): Unit =
        runBlocking {
            viewModel.description.postValue("let's be strong")
            viewModel.date.postValue("01/12/2030")

            val observer = spy<Observer<Unit>>()
            viewModel.taskSaved.observeForever(observer)

            viewModel.save()
            verify(observer).onChanged(eq(null))

        }

    /**
     * Scenario 3 - Add a new Task
     * Given the user is on the management of task page
     * When the user filled the description field
     * And the user write incorrectly in the date field
     * And click on the SAVE button
     * Then the user needs to see the message “Invalid date”
     */
    @Test
    fun `Then the user needs to see the message Invalid date`(): Unit =
        runBlocking {
            viewModel.description.postValue("A description")
            viewModel.date.postValue("99/99/9999")

            val observer = spy<Observer<Boolean>>()
            viewModel.showInvalidDateMessage.observeForever(observer)

            viewModel.save()
            verify(observer).onChanged(eq(true))

        }


}