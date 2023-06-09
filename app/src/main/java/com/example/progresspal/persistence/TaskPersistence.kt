package com.example.progresspal.persistence

import android.text.format.DateFormat
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * * Created by David Adane on 29/03/2023
 */

object TaskPersistence {
    var allTasks = ArrayList<Task>()
    var uncompletedTasksNewDay = ArrayList<Task>()
    var newDay = false
    var currentTimeStamp: Timestamp = Timestamp.now()
    lateinit var view : RecyclerView
    val database = Firebase.firestore


    fun get(view: RecyclerView, id: String){
        val docRef = database.collection("users").document(id).collection("currentDay").document("taskObjects")
        this.view = view
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null && document.data!!.isNotEmpty()) {
                    // value retrieved as hashmap because firebase provides objects in that form as
                    // seen in other examples in the code
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

                        var taskDate = Date(tempTask.dueDate.seconds * 1000)
                        var currentDate = Date()
                        val currentDay = (DateFormat.format("dd", currentDate) as String).toInt()
                        val taskDay = (DateFormat.format("dd", taskDate) as String).toInt()

                        // uncompletedTasksNewDay is used to store every task in that day while
                        // allTasks only stores the uncompleted ones to be displayed on the page
                        if (currentDate.after(taskDate) && taskDay != currentDay) {
                            currentTimeStamp = tempTask.dueDate
                            uncompletedTasksNewDay.add(tempTask)
                            if (tempTask.completed == false) {
                                allTasks.add(tempTask)
                                tempTask.dueDate = Timestamp.now()
                            }
                            newDay = true
                        } else {
                            allTasks.add(tempTask)
                        }
                    }

                    var priorityValues = ArrayList<String>()
                    priorityValues.add("High")
                    priorityValues.add("Medium")
                    priorityValues.add("Low")
                    allTasks.sortBy { task -> priorityValues.indexOf(task.priority) }
                    view.adapter?.notifyDataSetChanged()

                    // This dailyUpdate is here to update the task and archive page on a new day
                    if (newDay == true) {
                        newDay = false
                        dailyUpdate(id)
                    }
                    println("Completed task get")
                }
                else {
                    println("Document doesn't exist")
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }

    }

    fun create(task: Task, id: String) {
        val docRef = database.collection("users").document(id).collection("currentDay").document("taskObjects")
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
            .addOnFailureListener {
                // this is here because if the first add fails then it means there is no document
                // and update only works with an existing document
                val newObject: HashMap<String, ArrayList<Task>> = HashMap()
                val newUser: ArrayList<Task> = ArrayList()
                newUser.add(task)
                newObject.put("tasks", newUser)
                docRef
                    .set(newObject, SetOptions.merge())
                    .addOnSuccessListener {
                        println("Completed add")
                    }
                }
    }

    @JvmStatic
    fun edit(task: Task, position: Int, updateCompleted: Boolean, id: String) {
        val docRef = database.collection("users").document(id).collection("currentDay").document("taskObjects")
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

    fun updateAll(tasks: ArrayList<Task>, id: String){
        val docRef = database.collection("users").document(id).collection("currentDay").document("taskObjects")
        val newObject: HashMap<String, ArrayList<Task>> = HashMap()
        newObject.put("tasks", tasks)
        if(tasks.isNotEmpty()) {
            docRef
                .set(newObject)
                .addOnSuccessListener {
                    println("Completed edit")
                }
                .addOnFailureListener { println("failed") }
        }
        else{
            deleteAll(id)
        }
    }

    fun delete(position: Int, id: String){
        val docRef = database.collection("users").document(id).collection("currentDay").document("taskObjects")
        allTasks.removeAt(position)
        docRef
            .update("tasks", allTasks)
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }

    fun deleteAll(id: String){
        val docRef = database.collection("users").document(id).collection("currentDay").document("taskObjects")
        allTasks.clear()
        docRef
            .update("tasks", FieldValue.delete())
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }

    fun completedPercent(listOfTasks: ArrayList<Task>, newDay: Boolean, id: String): Int{
        var completedAmount: Int = 0
        for (task in listOfTasks) {
            if(task.completed){
                completedAmount += 1
            }
        }

        var completedPercent = 0
        if(listOfTasks.size != 0){
            val tempDouble: Double = (completedAmount.toDouble()/listOfTasks.size) * 100
            completedPercent = tempDouble.toInt()
            if(completedPercent >= 50 && newDay){
                ArchivePersistence.updateStreak(true, id)
            }
            else if(newDay){
                ArchivePersistence.updateStreak(false, id)
            }
        }
        return completedPercent
    }

    fun getDailyPercent(bar: ProgressBar, id: String): Int{
        var percent: Int = 0
        percent = completedPercent(allTasks, false, id)
        bar.secondaryProgress = percent
        return percent
    }


    fun dailyUpdate(id: String){
        updateAll(allTasks, id)
        //archivePersistence being called here so need to check if date must be changed first before updating list
        ArchivePersistence.create(uncompletedTasksNewDay, currentTimeStamp, id)
    }
}
