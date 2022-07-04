package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.lifecycle.*
import br.com.raynerweb.ipl.taskdone.core.SingleLiveEvent
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _toggleStatusFilter = SingleLiveEvent<Unit>()
    val toggleStatusFilter: LiveData<Unit> get() = _toggleStatusFilter

    private val _statusFilter = SingleLiveEvent<Status?>()

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList = MediatorLiveData<List<Task>>()

    private val _emptyTaskList = SingleLiveEvent<Unit>()
    val emptyTaskList: LiveData<Unit> get() = _emptyTaskList

    private val _taskDeleted = SingleLiveEvent<Pair<Task, Int>>()
    val taskDeleted: LiveData<Pair<Task, Int>> get() = _taskDeleted

    private val _taskShared = SingleLiveEvent<Task>()
    val taskShared: LiveData<Task> get() = _taskShared

    init {
        taskList.addSource(_statusFilter) {
            filter()
        }
    }

    private fun filter() {
        val list = mutableListOf<Task>()

        _taskList.value?.let { tasks ->
            _statusFilter.value?.let { status ->
                list.addAll(tasks.filter { task -> task.status == status })
            } ?: run {
                list.addAll(tasks)
            }
        }

        taskList.value = list
    }

    fun showStatusFilter() {
        _toggleStatusFilter.call()
    }

    fun setStatusFilter(status: Status?){
        _statusFilter.postValue(status)
    }

    fun deleteTask(task: Task, position: Int) = viewModelScope.launch {
        taskRepository.delete(task)
        _taskDeleted.postValue(Pair(task, position))
    }

    fun shareTask(task: Task) = viewModelScope.launch {
        _taskShared.postValue(task)
    }

    fun findAll() = viewModelScope.launch {
        val userTasks = userRepository.findAll()
        if (userTasks.isEmpty()) {
            _emptyTaskList.call()
            return@launch
        }
        val tasks = userTasks[0].tasks
        if (tasks.isEmpty()) {
            _emptyTaskList.call()
        } else {
            _taskList.postValue(tasks)
        }
    }

    fun checkEmptyList(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            _emptyTaskList.call()
        }
    }

}