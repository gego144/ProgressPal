package com.example.progresspal.persistence

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Archived
import com.example.progresspal.Model.ArchivedTask
import com.example.progresspal.Model.Task
import android.graphics.Color
import com.example.progresspal.R
import com.example.progresspal.databinding.ActivityStatsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Date

/**
 * * Created by David Adane on 02/04/2023
 */

object ArchivePersistence {
    var allArchives = ArrayList<Archived>()
    val database = Firebase.firestore

    fun get(view: RecyclerView, id: String): ArrayList<Archived> {
        val docRef = database.collection("users").document(id).collection("archivedDays")
            .document("currentDayObjects")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null && document.data!!.isNotEmpty()) {
                    // value retrieved as hashmap because firebase provides objects in that form as
                    // seen in other examples in the code
                    var databaseGrab = document.data?.get("archivedTasks") as ArrayList<HashMap<Any, Any>>
                    allArchives.clear()

                    for (i in 0 until databaseGrab.size) {
                        var tempTask = Archived(
                            databaseGrab.get(i).get("date") as Timestamp,
                            (databaseGrab.get(i).get("progress") as Long).toInt(),
                            databaseGrab.get(i).get("tasks") as java.util.ArrayList<ArchivedTask>
                        )

                        var tempArchivedTasks = tempTask.tasks as ArrayList<HashMap<Any, Any>>

                        for (j in 0 until tempArchivedTasks.size) {
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

    fun create(listOfTasks: ArrayList<Task>, currentDate: Timestamp, id: String){
        val docRef = database.collection("users").document(id).collection("archivedDays")
            .document("currentDayObjects")

        val archivedTaskList: ArrayList<ArchivedTask> = ArrayList()

        for(item in listOfTasks){
            val tempArchivedTask = ArchivedTask(
                item.title,
                item.completed
            )
            archivedTaskList.add(tempArchivedTask)
        }
        val archive = Archived(
            currentDate,
            TaskPersistence.completedPercent(listOfTasks, true, id),
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
                val newObject: HashMap<String, Any> = HashMap()
                val newUser: ArrayList<Archived> = ArrayList()
                newUser.add(archive)
                newObject.put("archivedTasks", newUser)
                newObject.put("streaks", 0)
                docRef
                    .set(newObject, SetOptions.merge())
                    .addOnSuccessListener {
                        println("Completed add")
                    }
            }
    }


    fun getStats(binding: ActivityStatsBinding, id: String) {
        val docRef = database.collection("users").document(id).collection("archivedDays")
            .document("currentDayObjects")

        var result = HashMap<Float, Float>()
        result[1f] = 0f
        result[2f] = 0f
        result[3f] = 0f
        result[4f] = 0f
        result[5f] = 0f
        result[6f] = 0f
        result[7f] = 0f
        result[8f] = 0f
        result[9f] = 0f
        result[10f] = 0f
        result[11f] = 0f
        result[12f] = 0f
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null && document.data!!.isNotEmpty()) {
                    var databaseGrab =
                        document.data?.get("archivedTasks") as ArrayList<HashMap<Any, Any>>
                    allArchives.clear()

                    for (i in 0 until databaseGrab.size) {
                        var tempTask = Archived(
                            databaseGrab.get(i).get("date") as Timestamp,
                            (databaseGrab.get(i).get("progress") as Long).toInt(),
                            databaseGrab.get(i).get("tasks") as java.util.ArrayList<ArchivedTask>
                        )

                        val date = Date(System.currentTimeMillis())
                        val year = date.year
                        val firestoreDate = tempTask.date.toDate()
                        firestoreDate.year
                        if (year == firestoreDate.year) {
                            val noOfTasks: Int = tempTask.tasks.size
                            val month: Int = firestoreDate.month + 1
                            val toBeUpdated = noOfTasks.toFloat()
                            result[month.toFloat()] = result[month.toFloat()]!! + toBeUpdated
                        }
                    }
                    val barArrayList = ArrayList<BarEntry>()
                    val barChart: BarChart = binding.barChart
                    val xAxisLabel: ArrayList<String> = ArrayList()
                    xAxisLabel.add("")
                    xAxisLabel.add("Jan")
                    xAxisLabel.add("Feb")
                    xAxisLabel.add("Mar")
                    xAxisLabel.add("Apr")
                    xAxisLabel.add("May")
                    xAxisLabel.add("Jun")
                    xAxisLabel.add("Jul")
                    xAxisLabel.add("Aug")
                    xAxisLabel.add("Sep")
                    xAxisLabel.add("Oct")
                    xAxisLabel.add("Nov")
                    xAxisLabel.add("Dec")
                    val xAxis: XAxis = barChart.xAxis
                    xAxis.setLabelCount(xAxisLabel.size, true)
                    xAxis.valueFormatter =
                        IAxisValueFormatter { value, axis -> xAxisLabel[value.toInt()] }
                    for ((key, value) in result) {
                        barArrayList.add(BarEntry(key, value))
                    }
                    val barDataSet: BarDataSet =
                        BarDataSet(barArrayList, "Completed tasks per month")
                    val barData: BarData = BarData(barDataSet)
                    barChart.data = barData
                    barDataSet.setValueTextColor(Color.BLACK)
                    barDataSet.valueTextSize = 10f
                    barDataSet.color = R.color.bar
                    binding.barChart.resetZoom()
                    println("Completed get statistics")
                } else {
                    println("Document doesn't exist")
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
    }

    fun delete(position: Int, id: String) {
        val docRef = database.collection("users").document(id).collection("archivedDays")
            .document("currentDayObjects")
        allArchives.removeAt(position)
        docRef
            .update("archivedTasks", allArchives)
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }

    fun updateStreak(passedStreak: Boolean, id: String){
        val docRef = database.collection("users").document(id).collection("archivedDays")
            .document("currentDayObjects")
        if(passedStreak) {
            docRef
                .update("streaks", FieldValue.increment(1))
                .addOnSuccessListener {
                    println("Completed updating streak")
                }
                .addOnFailureListener {
                    println("Failed update streak")
                }
        }
        else{
            docRef
                .update("streaks", 0)
                .addOnSuccessListener {
                    println("Completed updating streak")
                }
                .addOnFailureListener {
                    println("Failed update streak")
                }
        }
    }

    fun getStreaks(textview: TextView, id: String): Int{
        var streakCount: Int = 0
        val docRef = database.collection("users").document(id).collection("archivedDays")
            .document("currentDayObjects")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    var databaseGrab = document.data?.get("streaks") as Long
                    streakCount = databaseGrab.toInt()
                    textview.text = streakCount.toString()
                    println("Completed get streaks")
                } else {
                    println("Document doesn't exist")
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
        return streakCount
    }
}
