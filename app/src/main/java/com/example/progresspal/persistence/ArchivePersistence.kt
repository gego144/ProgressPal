package com.example.progresspal.persistence

import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Archived
import com.example.progresspal.Model.ArchivedTask
import com.example.progresspal.Model.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ArchivePersistence {
    var allArchives = ArrayList<Archived>()
    val database = Firebase.firestore

    val docRef = database.collection("users").document("testAndroidIDD").collection("archivedDays")
        .document("currentDayObjects")

    fun get(view: RecyclerView): ArrayList<Archived> {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
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

    fun create(listOfTasks: ArrayList<Task>){
        val archivedTaskList: ArrayList<ArchivedTask> = ArrayList()

        for(item in listOfTasks){
            val tempArchivedTask = ArchivedTask(
                item.title,
                item.completed
            )
            archivedTaskList.add(tempArchivedTask)
        }
        val archive = Archived(
            listOfTasks.get(0).dueDate,
            TaskPersistence.completedPercent(),
            archivedTaskList
        )

        docRef
            .update("archivedTasks", FieldValue.arrayUnion(archive))
            .addOnSuccessListener {
                println("Completed add")
            }
            .addOnFailureListener {
                // this is here because if the first add fails then it means there is no document
                // and update only works with an existing document
                val newObject: HashMap<String, ArrayList<Archived>> = HashMap()
                val newUser: ArrayList<Archived> = ArrayList()
                newUser.add(archive)
                newObject.put("archivedTasks", newUser)
                docRef
                    .set(newObject, SetOptions.merge())
                    .addOnSuccessListener {
                        println("Completed add")
                    }
            }
        //TaskPersistence.deleteAll()

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
