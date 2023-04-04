package com.example.progresspal.persistence

import android.text.format.DateFormat
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


object TaskPersistence{
    var allTasks = ArrayList<Task>()
    var uncompletedTasksNewDay = ArrayList<Task>()
    var newDay = false
    val uncompletedTasks: ArrayList<Task> = ArrayList()
    lateinit var view : RecyclerView
    val database = Firebase.firestore

    val docRef = database.collection("users").document("testAndroidIDD").collection("currentDay").document("taskObjects")

    fun get(view: RecyclerView){//: ArrayList<Task> {
        this.view = view
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null && document.data!!.isNotEmpty()) {
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
                        if(Date(tempTask.dueDate.seconds * 1000).after(Date())){
                            uncompletedTasksNewDay.add(tempTask)
                            if(tempTask.completed == false){
                                allTasks.add(tempTask)
                                tempTask.dueDate = Timestamp.now()
                                uncompletedTasks.add(tempTask)
                            }
                            newDay = true
                        }
                        else {
                            allTasks.add(tempTask)

                        }
                    }
                    view.adapter?.notifyDataSetChanged()

                    if(newDay == true){
                        updateArchive()
                    }

                    else {
                        /*newDay = true
                        for (item in uncompletedTasksNewDay) {
                            if (item.completed == false) {
                                item.dueDate = Timestamp.now()
                                uncompletedTasks.add(item)
                            }
                        }
                        allTasks = uncompletedTasks*/

                        println("its false")
                    }

                    /*if(uncompletedTasksNewDay.isNotEmpty()) {
                        println("ENTERS")

                        var taskDate: Date = Date(uncompletedTasksNewDay.get(0).dueDate.seconds * 1000)
                        var currentDate: Date = Date()
                        val currentDay = (DateFormat.format("dd", currentDate) as String).toInt()
                        val taskDay = (DateFormat.format("dd", taskDate) as String).toInt()

                        // archivePersistence being called here so need to check if date must be changed first before updating list
                        //if(currentDate.after(taskDate) && taskDay != currentDay){
                        if (taskDate.after(currentDate)) {
                            /*for (item in uncompletedTasksNewDay) {
                                if (item.completed == false) {
                                    item.dueDate = Timestamp.now()
                                    uncompletedTasks.add(item)
                                }
                            }
                            allTasks = uncompletedTasks
                            view.adapter?.notifyDataSetChanged()*/
                            updateAll(uncompletedTasks)
                            ArchivePersistence.create(uncompletedTasksNewDay)
                        } else {
                            view.adapter?.notifyDataSetChanged()
                        }

                        println("Completed get")
                    }*/
                } else {
                    println("Document doesn't exist")
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
        //return allTasks
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
    fun edit(task: Task, position: Int, updateCompleted: Boolean){
        if(updateCompleted){task.completed = !task.completed}
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

    fun updateAll(tasks: ArrayList<Task>){
        allTasks = tasks
        val newObject: HashMap<String, ArrayList<Task>> = HashMap()
        newObject.put("tasks", tasks)
        if(tasks.isNotEmpty()) {
            docRef
                .set(newObject)
                .addOnSuccessListener {
                    println("UPDATED")
                    //view.adapter?.notifyDataSetChanged()
                    println("Completed edit")
                }
                .addOnFailureListener { println("failed") }
        }
        else{
            deleteAll()
        }
    }

    fun delete(position: Int){
        allTasks.removeAt(position)
        docRef
            .update("tasks", allTasks)
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }

    fun deleteAll(){
        allTasks.clear()
        docRef
            .update("tasks", FieldValue.delete())
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }

    fun completedPercent(): Int{
        var completedAmount: Int = 0
        for (task in allTasks) {
            if(task.completed){
                completedAmount += 1
            }
        }

        var completedPercent = 0
        if(allTasks.size != 0){
            val tempDouble: Double = (completedAmount.toDouble()/allTasks.size) * 100
            completedPercent = tempDouble.toInt()
        }
        return completedPercent
    }

    fun updateArchive(){
        updateAll(allTasks)
        ArchivePersistence.create(uncompletedTasksNewDay)
    }
}
