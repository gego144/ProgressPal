package com.example.progresspal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.progresspal.persistence.TaskPersistence
import com.google.firebase.Timestamp
import java.sql.Time
import java.util.*


class editTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        val originalTask: Task
        var task: Task
        task = getIntent().getParcelableExtra("task")!!
        originalTask = getIntent().getParcelableExtra("task")!!
        var position = intent.getIntExtra("pos", 0)

        val taskName = findViewById<TextView>(R.id.editTaskName)
        taskName.text = task.title


        val datePickerOpen = findViewById<Button>(R.id.editDatePickerOpen)
        val date = Date(task.dueDate.seconds*1000)


        val stringDay = DateFormat.format("dd", date) as String
        val stringMonth = DateFormat.format("MM", date) as String
        val stringYear = DateFormat.format("yyyy", date) as String

        var day = stringDay.toInt()
        var month = stringMonth.toInt()
        var year = stringYear.toInt()

        var pickedDate = "$day-$month-$year"
        datePickerOpen.text = "$day-$month-$year"


        datePickerOpen.setOnClickListener(){
            val datePickerDialog = DatePickerDialog(this, android.R.style.Widget_CalendarView,
                { datePicker, year, month, day -> //Showing the picked value in the textView
                    val tempMonth = month + 1
                    pickedDate = "$day-$tempMonth-$year"
                    datePickerOpen.text = pickedDate
                    pickedDate = "$year-$month-$day"
                }, year, month-1, day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // priorities code
        var priority = "Low"
        val priorities = arrayOf("High", "Medium", "Low")
        val spinner = findViewById<Spinner>(R.id.editPrioritySpinner)
        val priorityAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorities)

        val spinnerPosition = priorityAdapter.getPosition(task.priority)
        spinner.adapter = priorityAdapter
        spinner.setSelection(spinnerPosition, true)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority = priorities[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("ok")
            }
        }


        // time picker code
        val timePickerOpen = findViewById<Button>(R.id.editTimePickerOpen)

        var numberTime : Int

        val time = Date(task.reminder.seconds*1000)
        val stringHour = DateFormat.format("HH", time) as String
        val stringMinute = DateFormat.format("mm", time) as String
        val amORpm = DateFormat.format("a", time) as String

        var hour = stringHour.toInt()
        var minute = stringMinute.toInt()

        if(amORpm == "PM"){
            if(hour == 24){
                hour = 0
            }
        }

        var pickedTime = "$hour:$minute"
        timePickerOpen.text = pickedTime

        timePickerOpen.setOnClickListener(){
            val timePickerDialog = TimePickerDialog(this,{ timePicker: TimePicker, i: Int, i1: Int ->
                pickedTime = "$i:$i1"
                numberTime = i + (i1/100)
                timePickerOpen.text = pickedTime
            }, hour, minute, true)

            timePickerDialog.show()
        }

        // repeat code
        var repeatWhen = "None"
        val repeat = arrayOf("None", "Daily", "Weekly", "Monthly")
        val repeatSpinner = findViewById<Spinner>(R.id.editRepeatSpinner)
        val repeatAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repeat)

        val repeatSpinnerPosition = repeatAdapter.getPosition(task.repeat)
        repeatSpinner.adapter = repeatAdapter
        repeatSpinner.setSelection(repeatSpinnerPosition, true)

        repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                repeatWhen = repeat[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        val intent = Intent(this, MainActivity::class.java)

        findViewById<Button>(R.id.editTaskDeleteBtn).setOnClickListener{
            //startActivity(intent)
        }


        findViewById<Button>(R.id.editTaskSaveChangesBtn).setOnClickListener{
            println(priority)
            println(repeatWhen)
            var date : Timestamp
            var time : Timestamp
            if(pickedDate == "$day-$month-$year"){
                date = task.dueDate
            }
            else{
                date = Timestamp(java.sql.Date.valueOf(pickedDate))
            }
            if(pickedTime == "$hour:$minute"){
                time = task.reminder
            }
            else{
                time = Timestamp(Time.valueOf(pickedTime+":00"))
            }
            val first = Task(0,"${taskName.text} \uD83D\uDE80", priority, date,time, repeatWhen,false );
            TaskPersistence.edit(first, position, originalTask)
            startActivity(intent)
        }

    }
}