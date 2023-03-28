package com.example.progresspal.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresspal.Task
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

    fun get(): ArrayList<Task> {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var databaseGrab = document.data?.get("tasks")
                    allTasks = databaseGrab as ArrayList<Task>
                    _myArrayList.value = allTasks
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

        val task = hashMapOf(
            "id" to allTasks.size,
            "Title" to task.title,
            "priority" to task.priority,
            "dueDate" to task.dueDate,
            "reminder" to task.reminder,
            "repeat" to task.repeat,
            "isCompleted" to task.completed,
        )

        docRef
            .update("tasks", FieldValue.arrayUnion(task))
            .addOnSuccessListener {
                println("Completed add")
            }
            .addOnFailureListener { println("failed") }

    }

}