package br.com.raynerweb.ipl.taskdone.ui.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raynerweb.ipl.taskdone.core.SingleLiveEvent
import br.com.raynerweb.ipl.taskdone.repository.TaskRepository
import br.com.raynerweb.ipl.taskdone.repository.UserRepository
import br.com.raynerweb.ipl.taskdone.ui.model.User
import br.com.raynerweb.ipl.taskdone.ui.model.ValidationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) : ViewModel() {


    val email = MutableLiveData("")
    val name = MutableLiveData("")

    private val _loginSuccess = SingleLiveEvent<Unit>()
    val loginSuccess: LiveData<Unit> get() = _loginSuccess

    private val _showEmailValidation = SingleLiveEvent<ValidationType>()
    val showEmailValidation: LiveData<ValidationType> get() = _showEmailValidation

    private val _showNameValidation = SingleLiveEvent<ValidationType>()
    val showNameValidation: LiveData<ValidationType> get() = _showNameValidation

    private fun getEmailText() = email.value ?: ""
    private fun getNameText() = name.value ?: ""

    private fun isValid(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(getEmailText())) {
            valid = valid && false
            _showEmailValidation.postValue(ValidationType.REQUIRED)
        }

        if (TextUtils.isEmpty(getNameText())) {
            valid = valid && false
            _showNameValidation.postValue(ValidationType.REQUIRED)
            return valid
        }

        return valid
    }

    fun googleLogin(name: String, email: String, photo: String?) = viewModelScope.launch {
        userRepository.save(
            User(
                name = name,
                email = email,
                photo = photo,
                isLocal = true
            )
        )

        userRepository.setLogged(true)
        userRepository.setTeam(true)
        _loginSuccess.call()
    }

    fun login() = viewModelScope.launch {

        if (!isValid()) {
            return@launch
        }

        userRepository.save(
            User(
                name = getNameText(),
                email = getEmailText(),
                isLocal = true
            )
        )

        userRepository.setLogged(true)
        userRepository.setTeam(false)
        _loginSuccess.call()

    }

}