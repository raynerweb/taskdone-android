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
     * Scenario 1 - Visualizar o Dashboard
     * Given I wanted to access the app
     * When I click on the TaskDone icon
     * Then I should see the dashboard screen
     * E uma opção de menu de Login
     */

    /**
     * Scenario 2 - Visualizar o Dashboard com dados
     * Given that I am on the dashboard screen
     * Quando eu tiver tarefas salvas no aplicativo
     * Then I should see a consolidation of the tasks that are in the To Do status,
     * And see a list of tasks that are in the In Progress status,
     * And I should see a consolidation of tasks that are in Done status
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

    /**
     * Scenario 3 - Visualizar o Dashboard sem dados
     * Given that I am on the dashboard screen
     * Quando eu não tiver tarefas salvas no aplicativo
     * Então eu devo ver a mensagem "No data to be displayed"
     * E o botão "Criar tarefas"
     */
    @Test
    fun `Entao eu devo ver a mensagem No data to be displayed`(): Unit =
        runBlocking {
            whenever(userRepository.findAll()).thenReturn(listOf(Mocks.USER_EMPTY_TASK))

            val observer = spy<Observer<List<Pair<Float, Status>>>>()
            viewModel.chartEntries.observeForever(observer)

            viewModel.getChartEntries()
            verify(observer).onChanged(eq(emptyList()))
        }

    /**
     * Scenario 4 - Ser direcionado ao login ao clicar no botao criar tarefas
     * Given that I am on the dashboard screen
     * Quando eu clicar no botão "Criar tarefas"
     * E não estiver logado na aplicação
     * Então eu devo ser direcionado para o ecrã de Login
     */

    /**
     * Scenario 5 - Ser direcionado ao login ao clicar na opção de menu criar tarefas
     * Given that I am on the dashboard screen
     * Quando eu clicar na opção de menu de Login
     * Então eu devo ser direcionado para o ecrã de Login
     */

    /**
     * Scenario 6 - Ser direcionado para visualizao das tarefas criadas
     * Given that I am on the dashboard screen
     * Quando eu clicar no botão "Criar tarefas"
     * E estiver logado na aplicação
     * Então eu devo ser direcionado para o ecrã Task List
     */
}