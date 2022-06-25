package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raynerweb.ipl.taskdone.core.SingleLiveEvent
import br.com.raynerweb.ipl.taskdone.ext.toDate
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.ui.model.Status
import br.com.raynerweb.ipl.taskdone.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _taskSaved = SingleLiveEvent<Unit>()
    val taskSaved: LiveData<Unit> get() = _taskSaved

    private val _descriptionValidation = MutableLiveData(true)
    val descriptionValidation: LiveData<Boolean> get() = _descriptionValidation

    private val _dateValidation = MutableLiveData(true)
    val dateValidation: LiveData<Boolean> get() = _dateValidation

    private val _status = MutableLiveData(Status.BACKLOG)
    val status: LiveData<Status> get() = _status

    val description = MutableLiveData("")
    val date = MutableLiveData("")

    private fun getDescriptionText() = description.value ?: ""
    private fun getDateText() = date.value ?: ""

    private fun isValid(): Boolean {
        var validation = true
        if (TextUtils.isEmpty(getDescriptionText())) {
            validation = validation || false
            _descriptionValidation.postValue(false)
        }
        if (TextUtils.isEmpty(getDateText())) {
            validation = validation || false
            _dateValidation.postValue(false)
        }
        return validation
    }

    fun save() = viewModelScope.launch {
        _descriptionValidation.postValue(true)
        _dateValidation.postValue(true)

        if (!isValid()) {
            return@launch
        }

        taskRepository.save(
            DEFAULT_USER,
            getDescriptionText(),
            getDateText().toDate(),
            status.value ?: Status.BACKLOG
        )

        _taskSaved.call()

    }

    companion object {
        private val DEFAULT_USER = User(name = "rayner", email = "email@email.com")
    }

    init {
        viewModelScope.launch {
            val savedUser = userRepository.findByEmail(DEFAULT_USER.email)
            if (savedUser == null) {
                userRepository.save(DEFAULT_USER)
            }
        }
    }

}