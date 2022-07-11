package br.com.raynerweb.ipl.taskdone.di

import android.content.Context
import android.content.SharedPreferences
import br.com.raynerweb.ipl.taskdone.repository.preference.LoginPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    private const val LOGIN_PREFERENCES = "LOGIN_PREFERENCES"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoginPreferences

    @Provides
    @LoginPreferences
    fun provide(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)

}