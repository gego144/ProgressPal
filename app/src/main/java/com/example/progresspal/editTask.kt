package com.example.progresspal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.progresspal.Model.Task
import com.example.progresspal.databinding.ActivityEditTaskBinding
import com.example.progresspal.persistence.TaskPersistence
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import java.sql.Time
import java.util.*


class editTask : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val originalTask: Task
        var task: Task
        task = getIntent().getParcelableExtra("task")!!
        originalTask = getIntent().getParcelableExtra("task")!!
        var position = intent.getIntExtra("pos", 0)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.menuBtn

        setSupportActionBar(toolbar);
        supportActionBar!!.setIcon(R.drawable.menu)

        navigationView.bringToFront()
        val toggle: ActionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)


        val taskName = binding.editTaskName
        taskName.setText(task.getTitle())


        val datePickerOpen = binding.editDatePickerOpen
        val date = Date(task.dueDate.seconds * 1000)


        val stringDay = DateFormat.format("dd", date) as String
        val stringMonth = DateFormat.format("MM", date) as String
        val stringYear = DateFormat.format("yyyy", date) as String

        var day = stringDay.toInt()
        var month = stringMonth.toInt()
        var year = stringYear.toInt()

        var pickedDate = "$day-$month-$year"
        datePickerOpen.text = "$day-$month-$year"


        datePickerOpen.setOnClickListener() {
            val datePickerDialog = DatePickerDialog(
                this, android.R.style.Widget_CalendarView,
                { datePicker, year, month, day -> //Showing the picked value in the textView
                    val tempMonth = month + 1
                    pickedDate = "$day-$tempMonth-$year"
                    datePickerOpen.text = pickedDate
                    pickedDate = "$year-$month-$day"
                }, year, month - 1, day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // priorities code
        var priority = "Low"
        val priorities = arrayOf("High", "Medium", "Low")
        val spinner = binding.editPrioritySpinner
        val priorityAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorities)

        val spinnerPosition = priorityAdapter.getPosition(task.priority)
        spinner.adapter = priorityAdapter
        spinner.setSelection(spinnerPosition, true)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority = priorities[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                print("ok")
            }
        }


        // time picker code
        val timePickerOpen = binding.editTimePickerOpen

        var numberTime: Int

        val time = Date(task.reminder.seconds * 1000)
        val stringHour = DateFormat.format("HH", time) as String
        val stringMinute = DateFormat.format("mm", time) as String
        val amORpm = DateFormat.format("a", time) as String

        var hour = stringHour.toInt()
        var minute = stringMinute.toInt()

        if (amORpm == "PM") {
            if (hour == 24) {
                hour = 0
            }
        }

        var pickedTime = "$hour:$minute"
        timePickerOpen.text = pickedTime

        timePickerOpen.setOnClickListener() {
            val timePickerDialog =
                TimePickerDialog(this, { timePicker: TimePicker, i: Int, i1: Int ->
                    pickedTime = "$i:$i1"
                    numberTime = i + (i1 / 100)
                    timePickerOpen.text = pickedTime
                }, hour, minute, true)

            timePickerDialog.show()
        }

        // repeat code
        var repeatWhen = "None"
        val repeat = arrayOf("None", "Daily", "Weekly", "Monthly")
        val repeatSpinner = binding.editRepeatSpinner
        val repeatAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repeat)

        val repeatSpinnerPosition = repeatAdapter.getPosition(task.repeat)
        repeatSpinner.adapter = repeatAdapter
        repeatSpinner.setSelection(repeatSpinnerPosition, true)

        repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                repeatWhen = repeat[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val intent = Intent(this, MainActivity::class.java)


        fun deleteTaskAlert(position: Int){

            val builder = AlertDialog.Builder(this)

            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                TaskPersistence.delete(position)
                startActivity(intent)
            }

            builder.setNegativeButton(android.R.string.no) { _, _ ->
                println("closed")
            }

            with(builder)
            {
                setTitle("Delete task")
                setMessage("Are you sure you want to delete?")
                show()
            }
        }

        binding.editTaskDeleteBtn.setOnClickListener {
            deleteTaskAlert(position)
        }


        binding.editTaskSaveChangesBtn.setOnClickListener {
            var date: Timestamp
            var time: Timestamp
            if (pickedDate == "$day-$month-$year") {
                date = task.dueDate
            } else {
                date = Timestamp(java.sql.Date.valueOf(pickedDate))
            }
            if (pickedTime == "$hour:$minute") {
                time = task.reminder
            } else {
                time = Timestamp(Time.valueOf(pickedTime + ":00"))
            }
            val first =
                Task(
                    0,
                    "${taskName.text}",
                    priority,
                    date,
                    time,
                    repeatWhen,
                    originalTask.completed
                );
            TaskPersistence.edit(first, position, false)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.archived -> {
                val intent = Intent(this, Archived::class.java)
                startActivity(intent)
            }
            R.id.stats -> {
                val intent = Intent(this, Stats::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}