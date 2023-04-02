package com.example.progresspal.persistence

import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Archived
import com.example.progresspal.Model.ArchivedTask
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ArchivePersistence {
    var allArchives = ArrayList<Archived>()
    val database = Firebase.firestore

    val docRef = database.collection("users").document("testAndroidID").collection("archivedDays")
        .document("currentDayObjects")

    fun get(view: RecyclerView): ArrayList<Archived> {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var databaseGrab = document.data?.get("archivedTasks") as ArrayList<HashMap<Any, Any>>
                    allArchives.clear()

                    for (i in 0 until databaseGrab.size) {
                        var tempTask = Archived(
                            databaseGrab.get(i).get("date") as Timestamp,
                            (databaseGrab.get(i).get("progress") as Long).toInt(),
                            databaseGrab.get(i).get("tasks") as java.util.ArrayList<ArchivedTask>
                        )

                        var tempArchivedTasks = tempTask.tasks as ArrayList<HashMap<Any, Any>>
                        println(tempArchivedTasks.size)

                        for(j in 0 until tempArchivedTasks.size){
                            val tempArchiveTask = ArchivedTask(
                                tempArchivedTasks.get(j).get("title") as String,
                                tempArchivedTasks.get(j).get("completed") as Boolean
                            )
                            tempTask.tasks[j] = tempArchiveTask
                        }
                        allArchives.add(tempTask)
                    }
                    view.adapter?.notifyDataSetChanged()
                    println("Completed get")
                } else {
                    println("Document doesn't exist")
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
        return allArchives
    }

    fun delete(position: Int){
        allArchives.removeAt(position)
        docRef
            .update("archivedTasks", allArchives)
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }
}
