package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.ext.toDate
import br.com.raynerweb.ipl.taskdone.mocks.Mocks
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.MOCK_FILTERS
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import java.util.*

class TaskListFiltersViewModelTest {

    private val taskRepository = mock<TaskRepository>()
    private val userRepository = mock<UserRepository>()

    @InjectMocks
    lateinit var viewModel: TaskListViewModel

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
     * Scenario 0 - Show filter by status
     * Given I am in task list screen
     * When I click on the funnel
     * Then I should see all Status option
     */
    //TODO TEST SCENARIO
    @Test
    fun `Then I should see all Status option`(): Unit =
        runBlocking {
            val toggleStatusFilterObserver = spy<Observer<Unit>>()
            viewModel.toggleStatusFilter.observeForever(toggleStatusFilterObserver)

            viewModel.showStatusFilter()

            verify(toggleStatusFilterObserver).onChanged(null)
        }


    /**
     * Scenario 1 - Filter by Status TO DO
     * Given that I am viewing all status options
     * When I click on the TO DO option
     * Then I should see all the tasks in the TO DO status
     */
    @Test
    fun `Then I should see all the tasks in the TO DO status`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            viewModel.setStatusFilter(Status.TODO)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS.filter { task -> task.status == Status.TODO }))
        }

    /**
     * Scenario 2 - Filter by IN PROGRESS status
     * Given that I am viewing all status options
     * When I click on the IN PROGRESS option
     * Then I should see all tasks in the IN PROGRESS status
     */
    @Test
    fun `Then I should see all tasks in the IN PROGRESS status`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            viewModel.setStatusFilter(Status.IN_PROGRESS)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS.filter { task -> task.status == Status.IN_PROGRESS }))
        }

    /**
     * Scenario 3 - Filter by DONE status
     * Given that I am viewing all status options
     * When I click on the DONE option
     * Then I should see all tasks in DONE status
     */
    @Test
    fun `Then I should see all tasks in DONE status`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            viewModel.setStatusFilter(Status.DONE)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS.filter { task -> task.status == Status.DONE }))
        }

    /**
     * Scenario 4 - Clear Filter by Status
     * Given that I am viewing all status options
     * When I want to clean the filter status
     * Then I should clik the menu Filter and choose the option ALL
     */
    @Test
    fun `Then I should clik the menu Filter and choose the option ALL`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            viewModel.setStatusFilter(null)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS))
        }

    /**
     * Scenario 5 - Filter by description
     * Given I am in task list screen
     * When I want to search for a task by description
     * Then I must click on the magnifier button
     * And enter a description in the search bar
     */
    @Test
    fun `Then I must click on the magnifier button And enter a description in the search bar`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))

            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            val description = "ca"
            viewModel.findAll()
            viewModel.setDescriptionFilter(description)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS.filter { task ->
                task.description.contains(
                    description
                )
            }))
        }

    /**
     * Scenario 6 - Clear Filter by Description
     * Given I am in task list screen
     * When I want to clear the search by description
     * Then I should click on the [X] in the search bar
     */
    @Test
    fun `Then I should click on the X in the search bar`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))

            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            val description = null
            viewModel.findAll()
            viewModel.setDescriptionFilter(description)

            verify(observer, times(2)).onChanged(eq(Mocks.MOCK_TASKS))
        }

    /**
     * Scenario 7 - Show date picket to filter by date
     * Given I am in task list screen
     * When I want to search for a task by date
     * Then I must click on the “Filter by date” from menu
     * And see the date picker component to select a date interval
     */
    @Test
    fun `And see the date picker component to select a date interval`(): Unit =
        runBlocking {
            val observer = spy<Observer<Unit>>()
            viewModel.toggleDateFilter.observeForever(observer)

            viewModel.showDateFilter()

            verify(observer).onChanged(null)
        }


    /**
     * Scenario 8 - Filter by date
     * Given I am in task list screen
     * When I want to search for a task by date
     * Then I must click on the calendar button
     * And choose the time period
     */
    @Test
    fun `Then I must click on the calendar button And choose the time period`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))

            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()


            val initial = "10/01/2022".toDate()
            val final = "20/01/2022".toDate()
            viewModel.setDateFilter(initial, final)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS.filter { task ->
                task.date.toDate().after(initial) && task.date.toDate().before(final)
            }))

        }

    /**
     * Scenario 9 - Clear Filter by Date
     * Given I am in task list screen
     * When I want to clear the search by date
     * Then I should click in the broom button
     */
    @Test
    fun `Then I should click in the broom button`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))

            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()

            viewModel.setDateFilter(null, null)

            verify(observer, times(2)).onChanged(eq(Mocks.MOCK_TASKS))
        }
}