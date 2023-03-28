package com.example.progresspal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class addTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // date picker code
        val datePickerOpen = findViewById<Button>(R.id.datePickerOpen)
        var pickedDate : String

        datePickerOpen.setOnClickListener(){
            val datePickerDialog = DatePickerDialog(this, android.R.style.Widget_CalendarView,
                { datePicker, year, month, day -> //Showing the picked value in the textView
                    pickedDate = "$day/$month/$year"
                    datePickerOpen.text = pickedDate
                }, 2023, 0, 20
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // priorities code
        val priorities = arrayOf("High", "Medium", "Low")
        val spinner = findViewById<Spinner>(R.id.prioritySpinner)
        val priorityAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        spinner.adapter = priorityAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println(priorities[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("ok")
            }
        }

        // time picker code
        val timePickerOpen = findViewById<Button>(R.id.timePickerOpen)
        var pickedTime : String

        timePickerOpen.setOnClickListener(){
            val timePickerDialog = TimePickerDialog(this,{ timePicker: TimePicker, i: Int, i1: Int -> //Showing the picked value in the textView
                pickedTime = "$i:$i1"
                timePickerOpen.text = pickedTime
            }, 12, 0, true)

            timePickerDialog.show()
        }

        // priorities code
        val repeat = arrayOf("None", "Daily", "Weekly", "Monthly")
        val repeatSpinner = findViewById<Spinner>(R.id.repeatSpinner)
        val repeatAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repeat)
        repeatSpinner.adapter = repeatAdapter
        repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println(repeat[p2])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("ok")
            }
        }

    }
}