package br.com.raynerweb.ipl.taskdone.repository.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.raynerweb.ipl.taskdone.di.SharedPreferencesModule
import javax.inject.Inject

class TeamPreference @Inject constructor(
    @SharedPreferencesModule.TeamPreferences
    private val preferences: SharedPreferences,
) {

    companion object {
        private const val TEAM = "TEAM"
    }

    /**
     * It is co working
     */
    var isTeam: Boolean
        get() = preferences.getBoolean(TEAM, false)
        set(value) = preferences.edit { putBoolean(TEAM, value) }
}