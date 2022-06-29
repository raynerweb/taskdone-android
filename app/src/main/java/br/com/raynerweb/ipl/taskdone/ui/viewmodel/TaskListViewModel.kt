package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raynerweb.ipl.taskdone.core.SingleLiveEvent
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.ui.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _taskList = SingleLiveEvent<List<Task>>()
    val taskList: LiveData<List<Task>> get() = _taskList

    private val _emptyTaskList = SingleLiveEvent<Unit>()
    val emptyTaskList: LiveData<Unit> get() = _emptyTaskList

    private val _taskDeleted = SingleLiveEvent<Pair<Task, Int>>()
    val taskDeleted: LiveData<Pair<Task, Int>> get() = _taskDeleted

    private val _taskShared = SingleLiveEvent<Task>()
    val taskShared: LiveData<Task> get() = _taskShared

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