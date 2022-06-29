package br.com.raynerweb.ipl.taskdone.di

import br.com.raynerweb.ipl.taskdone.repository.firebase.realtime.TaskReferenceRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = Firebase.database

    @Singleton
    @Provides
    fun provideTaskReference(firebaseDatabase: FirebaseDatabase) = firebaseDatabase.getReference("tasks")

    @Singleton
    @Provides
    fun provideTaskReferenceRepository(taskReference: DatabaseReference) = TaskReferenceRepository(taskReference)

}