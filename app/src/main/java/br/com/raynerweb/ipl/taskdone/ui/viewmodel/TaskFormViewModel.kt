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
import java.text.ParseException
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _taskSaved = SingleLiveEvent<Unit>()
    val taskSaved: LiveData<Unit> get() = _taskSaved

    private val _showRequiredDescriptionMessage = MutableLiveData(false)
    val showRequiredDescriptionMessage: LiveData<Boolean> get() = _showRequiredDescriptionMessage

    private val _showRequiredDateMessage = MutableLiveData(false)
    val showRequiredDateMessage: LiveData<Boolean> get() = _showRequiredDateMessage

    private val _showInvalidDateMessage = MutableLiveData(false)
    val showInvalidDateMessage: LiveData<Boolean> get() = _showInvalidDateMessage

    private val _status = MutableLiveData(Status.BACKLOG)
    val status: LiveData<Status> get() = _status

    val description = MutableLiveData("")
    val date = MutableLiveData("")

    private fun getDescriptionText() = description.value ?: ""
    private fun getDateText() = date.value ?: ""

    private fun isValid(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(getDescriptionText())) {
            valid = valid && false
            _showRequiredDescriptionMessage.postValue(true)
        }

        if (TextUtils.isEmpty(getDateText())) {
            valid = valid && false
            _showRequiredDateMessage.postValue(true)
            return valid
        }

        try {
            getDateText().toDate()
        } catch (e: ParseException) {
            valid = valid && false
            _showInvalidDateMessage.postValue(true)
        }

        return valid
    }

    private fun isDateValid(): Boolean {
        var valid = true
        try {
            getDateText().toDate()
        } catch (e: ParseException) {
            valid = valid && false
            _showInvalidDateMessage.postValue(true)
        }
        return valid
    }

    fun save() = viewModelScope.launch {
        _showInvalidDateMessage.postValue(false)
        _showRequiredDescriptionMessage.postValue(false)
        _showRequiredDateMessage.postValue(false)

        if (!isValid()) {
            return@launch
        }

        if (!isDateValid()) {
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

    fun setStatus(status: Status) {
        _status.postValue(status)
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