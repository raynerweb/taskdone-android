package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.TASK
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER_EMPTY_TASK
import br.com.raynerweb.ipl.taskdone.mocks.Mocks.USER_TASK
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
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

class TaskListViewModelTest {

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
     * Scenario 2 - Task List
     * Given the user is on the task list screen
     * When the task list is empty
     * Then the user needs to see a message: "You don’t have a task here"
     */
    @Test
    fun `Then the user needs to see a message You dont have a task here`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(USER_EMPTY_TASK))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            verify(observer).onChanged(USER_EMPTY_TASK.tasks)
        }

    /**
     * Scenario 2 - Task List empty user
     * Given the user is on the task list screen
     * When the task list is empty
     * Then the user needs to see a message: "You don’t have a task here"
     */
    @Test
    fun `Scenario 2 - Task List empty user - Then the user needs to see a message You dont have a task here`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(emptyList())
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            verify(observer).onChanged(USER_EMPTY_TASK.tasks)
        }

    /**
     * Scenario 3 - Task List
     * Given the user is on the task list screen
     * When the user has tasks to be shown
     * Then the user needs to see a message: "You don’t have a task here"
     */
    @Test
    fun `Then the task list screen needs to show your tasks`(): Unit =
        runBlocking {

            whenever(userRepository.findAll()).thenReturn(listOf(USER_TASK))
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            verify(observer).onChanged(eq(listOf(TASK)))

        }

    /**
     * Scenario 3 - Delete Task
     * Given the user is on the task list page
     * When the user clicks on the trash icon from the task item
     * Then the task is deleted
     */
    @Test
    fun `Then the task is deleted`(): Unit =
        runBlocking {
            whenever(taskRepository.delete(any())).thenReturn(Unit)

            val taskDeletedObsever = spy<Observer<Pair<Task, Int>>>()
            viewModel.taskDeleted.observeForever(taskDeletedObsever)

            viewModel.deleteTask(TASK, 1)

            verify(taskDeletedObsever).onChanged(eq(Pair(TASK, 1)))
        }

    /**
     * Scenario 3 - Delete Task
     * Given the user is on the task list page
     * When the user clicks on the trash icon from the task item
     * Then the task is deleted and empty list need to be shown
     */
    @Test
    fun `Then the task is deleted and empty list need to be shown`(): Unit =
        runBlocking {
            val deleteObserver = spy<Observer<Pair<Task, Int>>>()
            viewModel.taskDeleted.observeForever(deleteObserver)

            viewModel.deleteTask(TASK, 1)

            verify(deleteObserver).onChanged(eq(Pair(TASK, 1)))

        }

    /**
     * Scenario 3 - Delete Task
     * Given the user is on the task list page
     * When the user clicks on the trash icon from the task item
     * Then the task is deleted and empty list need to be shown
     */
    @Test
    fun `Then the task is deleted and empty list cannot be called`(): Unit =
        runBlocking {
            val deleteObserver = spy<Observer<Pair<Task, Int>>>()
            viewModel.taskDeleted.observeForever(deleteObserver)

            viewModel.deleteTask(TASK, 1)

            verify(deleteObserver).onChanged(eq(Pair(TASK, 1)))
        }


    /**
     * Scenario 1 - Share the task
     * Given the user is on the task list page
     * When the user clicks on the share button
     * Then the user can share the task
     */
    @Test
    fun `Then the user can share the task`(): Unit =
        runBlocking {
            val shareObserver = spy<Observer<Task>>()
            viewModel.taskShared.observeForever(shareObserver)

            viewModel.shareTask(TASK)

            verify(shareObserver).onChanged(eq(TASK))
        }


}