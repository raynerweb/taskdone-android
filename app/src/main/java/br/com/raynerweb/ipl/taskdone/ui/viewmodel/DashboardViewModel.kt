package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.lifecycle.*
import br.com.raynerweb.ipl.taskdone.core.SingleLiveEvent
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _showLogin = SingleLiveEvent<Unit>()
    val showLogin: LiveData<Unit> get() = _showLogin

    private val _isLogged = SingleLiveEvent<Boolean>()
    val isLogged: LiveData<Boolean> get() = _isLogged

    private val _chartEntries = SingleLiveEvent<List<Pair<Float, Status>>>()
    val chartEntries: LiveData<List<Pair<Float, Status>>> get() = _chartEntries

    private fun getPercent(tasks: List<Task>, status: Status): Float {
        return if ((tasks.filter { task -> task.status == status }.size.toFloat()
                .div(tasks.size.toFloat()) * 100).isNaN()
        ) {
            0.toFloat()
        } else {
            (tasks.filter { task -> task.status == status }.size.toFloat()
                .div(tasks.size.toFloat()) * 100).roundToInt().toFloat()
        }
    }

    fun showLoginScreen() {
        _showLogin.call()
    }

    fun createTask() {
        _isLogged.postValue(userRepository.isLogged())
    }

    fun getChartEntries() = viewModelScope.launch {
        val userTasks = userRepository.findAll()
        if (userTasks.isEmpty()) {
            _chartEntries.postValue(emptyList())
            return@launch
        }
        val tasks = mutableListOf<Task>()
        userTasks.forEach {
            tasks.addAll(it.tasks)
        }
        if (tasks.isEmpty()) {
            _chartEntries.postValue(emptyList())
            return@launch
        }
        val todoList = getPercent(tasks, Status.TODO)
        val progressList = getPercent(tasks, Status.IN_PROGRESS)
        val doneList = getPercent(tasks, Status.DONE)

        _chartEntries.postValue(
            listOf(
                Pair(
                    todoList,
                    Status.TODO,
                ),
                Pair(
                    progressList,
                    Status.IN_PROGRESS,
                ),
                Pair(
                    doneList,
                    Status.DONE,
                ),
            )
        )
    }

}