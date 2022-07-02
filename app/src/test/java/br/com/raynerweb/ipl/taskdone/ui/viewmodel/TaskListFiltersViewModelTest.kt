package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
            whenever(userRepository.findAll()).thenReturn(listOf(MOCK_FILTERS))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            viewModel.statusFilter.postValue(Status.TODO)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS.filter { task -> task.status == Status.TODO }))
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
            viewModel.statusFilter.postValue(Status.TODO)

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
            viewModel.statusFilter.postValue(Status.IN_PROGRESS)

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
            viewModel.statusFilter.postValue(Status.DONE)

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
            viewModel.statusFilter.postValue(null)

            verify(observer).onChanged(eq(Mocks.MOCK_TASKS))
        }

}