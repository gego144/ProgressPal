package com.example.progresspal.persistence

import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Archived
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
                            databaseGrab.get(i).get("day") as Timestamp,
                            (databaseGrab.get(i).get("progressPercent") as Long).toInt(),
                            databaseGrab.get(i).get("tasks") as java.util.ArrayList<java.util.HashMap<String, com.google.protobuf.Any>>
                        )
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
}