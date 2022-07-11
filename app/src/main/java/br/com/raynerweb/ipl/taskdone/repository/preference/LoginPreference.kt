package br.com.raynerweb.ipl.taskdone.repository.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.raynerweb.ipl.taskdone.di.SharedPreferencesModule
import javax.inject.Inject

class LoginPreference @Inject constructor(
    @SharedPreferencesModule.LoginPreferences
    private val preferences: SharedPreferences,
) {

    companion object {
        private const val LOGIN = "LOGIN"
    }

    /**
     * Is logged
     */
    var isLogged: Boolean
        get() = preferences.getBoolean(LOGIN, false) ?: false
        set(value) = preferences.edit { putBoolean(LOGIN, value) }
}