package com.example.progresspal.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TaskPersistence : ViewModel(){
    var allTasks = ArrayList<Task>()
    val database = Firebase.firestore
    private val _myArrayList: MutableLiveData<ArrayList<Task>> = MutableLiveData()

    val myArrayList: LiveData<ArrayList<Task>>
        get() = _myArrayList

    val docRef = database.collection("users").document("testAndroidID").collection("currentDay").document("taskObjects")

    fun get(view: RecyclerView): ArrayList<Task> {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var databaseGrab = document.data?.get("tasks") as ArrayList<HashMap<Any, Any>>

                    allTasks.clear()

                    for (i in 0 until databaseGrab.size) {
                        var tempTask = Task(
                            databaseGrab.size,
                            databaseGrab.get(i).get("title") as String?,
                            databaseGrab.get(i).get("priority") as String?,
                            databaseGrab.get(i).get("dueDate") as Timestamp?,
                            databaseGrab.get(i).get("reminder") as Timestamp?,
                            databaseGrab.get(i).get("repeat") as String?,
                            databaseGrab.get(i).get("completed") as Boolean?,
                        )
                        allTasks.add(tempTask)
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
        return allTasks
    }

    fun create(task: Task){

        val task = Task(
            allTasks.size,
            task.title,
            task.priority,
            task.dueDate,
            task.reminder,
            task.repeat,
            task.completed,
        )
        allTasks.add(task);
        docRef
            .update("tasks", FieldValue.arrayUnion(task))
            .addOnSuccessListener {
                println("Completed add")
            }
            .addOnFailureListener { println("failed") }

    }

}