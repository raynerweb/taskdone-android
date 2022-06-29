package br.com.raynerweb.ipl.taskdone.repository.firebase.realtime

import android.util.Log
import br.com.raynerweb.ipl.taskdone.repository.firebase.realtime.model.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

class TaskReferenceRepository constructor(val taskReference: DatabaseReference) {

    init {
        taskReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())
                val tasks = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue<Task>()
                }
                Log.i("SNAPSHOT", tasks.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("SNAPSHOT", error.details)
            }
        })
    }

    fun save(description: String, date: Date, status: String): String {
        val uuid = UUID.randomUUID().toString()
        taskReference.setValue(
            Pair(
                uuid,
                Task(description = description, date = date, status = status, creationDate = Date())
            )
        )
        return uuid
    }
}