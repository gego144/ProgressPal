package com.example.progresspal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.progresspal.persistence.TaskPersistence
import com.google.firebase.Timestamp
import java.sql.Time
import java.sql.Date
import java.util.*

class addTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)


        // date picker code
        val datePickerOpen = findViewById<Button>(R.id.datePickerOpen)
        var pickedDate = ""

        datePickerOpen.setOnClickListener(){
            val datePickerDialog = DatePickerDialog(this, android.R.style.Widget_CalendarView,
                { datePicker, year, month, day -> //Showing the picked value in the textView
                    pickedDate = "$day-$month-$year"
                    datePickerOpen.text = pickedDate
                    pickedDate = "$year-$month-$day"
                }, 2023, 0, 20
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // priorities code
        var priority = "High"
        val priorities = arrayOf("High", "Medium", "Low")
        val spinner = findViewById<Spinner>(R.id.prioritySpinner)
        val priorityAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        spinner.adapter = priorityAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority = priorities[p2]
                println(priorities[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("ok")
            }
        }

        // time picker code
        val timePickerOpen = findViewById<Button>(R.id.timePickerOpen)
        var pickedTime = ""
        var numberTime : Int

        timePickerOpen.setOnClickListener(){
            val timePickerDialog = TimePickerDialog(this,{ timePicker: TimePicker, i: Int, i1: Int -> //Showing the picked value in the textView
                pickedTime = "$i:$i1"
                numberTime = i + (i1/100)
                timePickerOpen.text = pickedTime
            }, 12, 0, true)

            timePickerDialog.show()
        }

        // repeat code
        var repeatWhen = "None"
        val repeat = arrayOf("None", "Daily", "Weekly", "Monthly")
        val repeatSpinner = findViewById<Spinner>(R.id.repeatSpinner)
        val repeatAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repeat)
        repeatSpinner.adapter = repeatAdapter
        repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                repeatWhen = repeat[p2]
                println(repeat[p2])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("ok")
            }
        }
        val intent = Intent(this, MainActivity::class.java)

        findViewById<Button>(R.id.addTaskDeleteBtn).setOnClickListener{
            startActivity(intent)
        }

        val taskName = findViewById<TextView>(R.id.taskName).text;

        findViewById<Button>(R.id.addTaskSaveChangesBtn).setOnClickListener{
            var date : Timestamp
            var time : Timestamp
            if(pickedDate == ""){
                date = Timestamp(Date(System.currentTimeMillis()))
            }
            else{
                date = Timestamp(Date.valueOf(pickedDate))
            }
            if(pickedTime == ""){
                time = Timestamp(Time.valueOf("12:00:00"))
            }
            else{
                time = Timestamp(Time.valueOf(pickedTime+":00"))
            }

            val first = Task(0,"$taskName \uD83D\uDE80", priority, date,time, repeatWhen,false );
            TaskPersistence.create(first)
            startActivity(intent)
        }
    }
}