package com.example.progresspal.persistence

import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TaskPersistence {
    var allTasks = ArrayList<Task>()
    val database = Firebase.firestore

    val docRef = database.collection("users").document("testAndroidID").collection("currentDay")
        .document("taskObjects")

    fun get(view: RecyclerView): ArrayList<Task> {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var databaseGrab = document.data?.get("tasks") as ArrayList<HashMap<Any, Any>>

                    allTasks.clear()

                    for (i in 0 until databaseGrab.size) {
                        var tempTask = Task(
                            i,
                            databaseGrab.get(i).get("title") as String?,
                            databaseGrab.get(i).get("priority") as String?,
                            databaseGrab.get(i).get("dueDate") as Timestamp?,
                            databaseGrab.get(i).get("reminder") as Timestamp?,
                            databaseGrab.get(i).get("repeat") as String?,
                            databaseGrab.get(i).get("completed") as Boolean?,
                        )
                        allTasks.add(tempTask)
                    }
                    var priorityValues = ArrayList<String>()
                    priorityValues.add("High")
                    priorityValues.add("Medium")
                    priorityValues.add("Low")
                    allTasks.sortBy { task -> priorityValues.indexOf(task.priority) }
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

    fun create(task: Task) {
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

    @JvmStatic
    fun edit(task: Task, position: Int, updateCompleted: Boolean) {
        if (updateCompleted) {
            task.completed = !task.completed
        }
        val task = Task(
            allTasks.size,
            task.title,
            task.priority,
            task.dueDate,
            task.reminder,
            task.repeat,
            task.completed,
        )
        allTasks.set(position, task)
        docRef
            .update("tasks", allTasks)
            .addOnSuccessListener {
                println("Completed edit")
            }
            .addOnFailureListener { println("failed") }
    }

    fun delete(position: Int) {

        allTasks.removeAt(position)
        docRef
            .update("tasks", allTasks)
            .addOnSuccessListener {
                println("Completed edit")
            }
            .addOnFailureListener { println("failed") }
    }

}
