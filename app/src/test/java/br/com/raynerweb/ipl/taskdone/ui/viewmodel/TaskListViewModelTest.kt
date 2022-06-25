package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.test.CoroutineTestRule
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import br.com.raynerweb.ipl.taskdone.ui.model.User
import br.com.raynerweb.ipl.taskdone.ui.model.UserTasks
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
            whenever(userRepository.findAll()).thenReturn(userWithEmptyList())
            val observer = spy<Observer<Unit>>()
            viewModel.emptyTaskList.observeForever(observer)

            viewModel.findAll()
            verify(observer).onChanged(null)
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

            whenever(userRepository.findAll()).thenReturn(userWithTasks())
            val observer = spy<Observer<List<Task>>>()
            viewModel.taskList.observeForever(observer)

            viewModel.findAll()
            verify(observer).onChanged(eq(tasks()))

        }

    private fun userWithEmptyList() =
        listOf(UserTasks(user = user(), tasks = emptyList()))

    private fun userWithTasks() =
        listOf(
            UserTasks(
                user = user(), tasks = tasks()
            )
        )

    private fun user() = User(name = "", email = "")

    private fun tasks() = listOf(
        Task(
            taskId = "0",
            description = "description",
            date = "01/01/1900",
            status = Status.BACKLOG
        )
    )

}