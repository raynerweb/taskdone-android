package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.lifecycle.*
import br.com.raynerweb.ipl.taskdone.core.SingleLiveEvent
import br.com.raynerweb.ipl.taskdone.ext.toDate
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _dateFilterActive = MutableLiveData(false)
    val dateFilterActive: LiveData<Boolean> get() = _dateFilterActive

    private val _toggleDateFilter = SingleLiveEvent<Unit>()
    val toggleDateFilter: LiveData<Unit> get() = _toggleDateFilter

    private val _toggleStatusFilter = SingleLiveEvent<Unit>()
    val toggleStatusFilter: LiveData<Unit> get() = _toggleStatusFilter

    private val _dateFilter = SingleLiveEvent<Pair<Date, Date>?>()
    private val _descriptionFilter = SingleLiveEvent<String?>()
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
        taskList.addSource(_taskList) {
            filter()
        }
        taskList.addSource(_statusFilter) {
            filter()
        }
        taskList.addSource(_descriptionFilter) {
            filter()
        }
        taskList.addSource(_dateFilter) {
            filter()
        }
    }

    private fun filter() {
        var filtered = listOf<Task>()

        _taskList.value?.let { tasks ->
            filtered = tasks

            _statusFilter.value?.let { status ->
                filtered = tasks.filter { task -> task.status == status }
            }

            _descriptionFilter.value?.let { description ->
                filtered = filtered.filter { task -> task.description.contains(description) }
            }

            _dateFilter.value?.let { dateInterval ->
                filtered = filtered.filter { task ->
                    task.date.toDate().after(dateInterval.first) &&
                            task.date.toDate().before(dateInterval.second)
                }
            }
        }

        taskList.value = filtered
    }

    fun showStatusFilter() {
        _toggleStatusFilter.call()
    }

    fun setStatusFilter(status: Status?) {
        _statusFilter.postValue(status)
    }

    fun setDescriptionFilter(description: String?) {
        _descriptionFilter.postValue(description)
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

    fun setDateFilter(initial: Date?, final: Date?) {
        if (initial != null && final != null) {
            _dateFilter.postValue(Pair(initial, final))
            _dateFilterActive.postValue(true)
        } else {
            _dateFilter.postValue(null)
            _dateFilterActive.postValue(false)
        }

    }

    fun showDateFilter() {
        _toggleDateFilter.call()
    }

}