package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.mocks.Mocks
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import com.github.mikephil.charting.data.PieEntry
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class DashboardViewModelTest {

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


    /**
     * Scenario 2 - View the Dashboard without Data
     * Given that I am at the dashboard screen
     * When I have no tasks saved in the application
     * Then I should see the message "No data to display"
     * And the "Create Tasks" button
     */
    @Test
    fun `Then I should see the message No data to display`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(Mocks.USER_EMPTY_TASK))

            val observer = spy<Observer<List<Pair<Float, Status>>>>()
            viewModel.chartEntries.observeForever(observer)

            viewModel.getChartEntries()
            verify(observer).onChanged(eq(emptyList()))
        }

    /**
     * Scenario 3 - Being Redirected to Login
     * Given I am on the dashboard screen
     * When I click on the "Create Tasks" button
     * And I am not logged into the application
     * Then I should be redirected to the Login screen
     */
    @Test
    fun `Then I should be redirected to the Login screen`(): Unit =
        runBlocking {
            whenever(userRepository.isLogged()).thenReturn(false)

            val observer = spy<Observer<Boolean>>()
            viewModel.isLogged.observeForever(observer)

            viewModel.createTask()
            verify(observer).onChanged(eq(false))
        }

    /**
     * Scenario 4 - Login Navigation
     * Given I am on the dashboard screen
     * When I click on the Login menu option
     * Then I should be redirected to the Login screen
     */
    @Test
    fun `When I click on the Login menu option Then I should be redirected to the Login screen`(): Unit =
        runBlocking {
            val observer = spy<Observer<Unit>>()
            viewModel.showLogin.observeForever(observer)

            viewModel.showLoginScreen()
            verify(observer).onChanged(eq(null))
        }

    /**
     * Scenario 5 - Be directed to view the tasks that have been created
     * Given I am on the dashboard screen
     * When I click on the "Create Tasks" button
     * And I am logged into the application
     * Then I should be directed to the Task List screen
     */
    @Test
    fun `Then I should be directed to the Task List screen`(): Unit =
        runBlocking {
            whenever(userRepository.isLogged()).thenReturn(true)

            val observer = spy<Observer<Boolean>>()
            viewModel.isLogged.observeForever(observer)

            viewModel.createTask()
            verify(observer).onChanged(eq(true))
        }

    /**
     * Scenario 6 - Viewing the Dashboard with Data
     * Given I am on the dashboard screen
     * When I have tasks saved in the application
     * Then I should see a graph with the percentage of tasks separated by Status
     */
    @Test
    fun `Then I should see a consolidation of the tasks that are in the To Do status`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(Mocks.MOCK_FILTERS))

            val observer = spy<Observer<List<Pair<Float, Status>>>>()
            viewModel.chartEntries.observeForever(observer)

            viewModel.getChartEntries()

            verify(observer).onChanged(
                eq(
                    listOf(
                        Pair(
                            33.toFloat(),
                            Status.TODO,
                        ),
                        Pair(
                            50.toFloat(),
                            Status.IN_PROGRESS,
                        ),
                        Pair(
                            17.toFloat(),
                            Status.DONE,
                        ),
                    )
                )
            )
        }


}