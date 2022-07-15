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
import br.com.raynerweb.ipl.taskdone.ui.model.ValidationType
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

    private val _showDescriptionValidation = SingleLiveEvent<ValidationType>()
    val showDescriptionValidation: LiveData<ValidationType> get() = _showDescriptionValidation

    private val _showDateValidation = SingleLiveEvent<ValidationType>()
    val showDateValidation: LiveData<ValidationType> get() = _showDateValidation

    private val _status = MutableLiveData(Status.TODO)
    val status: LiveData<Status> get() = _status

    val description = MutableLiveData("")
    val date = MutableLiveData("")

    private fun getDescriptionText() = description.value ?: ""
    private fun getDateText() = date.value ?: ""

    private fun isValid(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(getDescriptionText())) {
            valid = valid && false
            _showDescriptionValidation.postValue(ValidationType.REQUIRED)
        }

        if (TextUtils.isEmpty(getDateText())) {
            valid = valid && false
            _showDateValidation.postValue(ValidationType.REQUIRED)
            return valid
        }

        try {
            getDateText().toDate()
        } catch (e: ParseException) {
            valid = valid && false
            _showDateValidation.postValue(ValidationType.INVALID_FIELD)
        }

        return valid
    }

    fun save() = viewModelScope.launch {
        _showDescriptionValidation.postValue(ValidationType.VALID)
        _showDateValidation.postValue(ValidationType.VALID)

        if (!isValid()) {
            return@launch
        }

        taskRepository.save(
            getDescriptionText(),
            getDateText().toDate(),
            status.value ?: Status.TODO
        )

        _taskSaved.call()

    }

    fun setStatus(status: Status) {
        _status.postValue(status)
    }

}