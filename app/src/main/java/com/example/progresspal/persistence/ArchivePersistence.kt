package com.example.progresspal.persistence

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Model.Archived
import com.example.progresspal.Model.ArchivedTask
import com.example.progresspal.R
import com.example.progresspal.databinding.ActivityStatsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import java.sql.Date

object ArchivePersistence {
    var allArchives = ArrayList<Archived>()
    val database = Firebase.firestore

    val docRef = database.collection("users").document("testAndroidID").collection("archivedDays")
        .document("currentDayObjects")

    fun get(view: RecyclerView): ArrayList<Archived> {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var databaseGrab =
                        document.data?.get("archivedTasks") as ArrayList<HashMap<Any, Any>>
                    allArchives.clear()

                    for (i in 0 until databaseGrab.size) {
                        var tempTask = Archived(
                            databaseGrab.get(i).get("date") as Timestamp,
                            (databaseGrab.get(i).get("progress") as Long).toInt(),
                            databaseGrab.get(i).get("tasks") as java.util.ArrayList<ArchivedTask>
                        )

                        var tempArchivedTasks = tempTask.tasks as ArrayList<HashMap<Any, Any>>
                        println(tempArchivedTasks.size)

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

    fun getStats(binding: ActivityStatsBinding) {
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
                if (document != null) {
                    var databaseGrab =
                        document.data?.get("archivedTasks") as ArrayList<HashMap<Any, Any>>
                    allArchives.clear()
                    println(databaseGrab.size)
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

    fun delete(position: Int) {
        allArchives.removeAt(position)
        docRef
            .update("archivedTasks", allArchives)
            .addOnSuccessListener {
                println("Completed delete")
            }
            .addOnFailureListener { println("failed") }
    }
}
