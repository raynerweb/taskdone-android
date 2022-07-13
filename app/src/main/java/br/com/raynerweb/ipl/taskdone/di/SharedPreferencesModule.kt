package br.com.raynerweb.ipl.taskdone.di

import android.content.Context
import android.content.SharedPreferences
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
    private const val TEAM_PREFERENCES = "TEAM_PREFERENCES"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoginPreferences

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TeamPreferences

    @Provides
    @LoginPreferences
    fun provideLoginPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @TeamPreferences
    fun provideTeamPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(TEAM_PREFERENCES, Context.MODE_PRIVATE)

}